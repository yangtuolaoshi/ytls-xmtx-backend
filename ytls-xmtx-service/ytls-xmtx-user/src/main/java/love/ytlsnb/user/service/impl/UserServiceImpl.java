package love.ytlsnb.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.*;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import love.ytls.api.school.SchoolClient;
import love.ytlsnb.common.constants.RedisConstant;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.UserConstant;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.properties.JwtProperties;
import love.ytlsnb.common.properties.UserProperties;
import love.ytlsnb.common.utils.AliUtil;
import love.ytlsnb.common.utils.ColadminHolder;
import love.ytlsnb.common.utils.JwtUtil;
import love.ytlsnb.common.utils.UserHolder;
import love.ytlsnb.model.school.po.Coladmin;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.school.po.Clazz;
import love.ytlsnb.model.school.po.Dept;
import love.ytlsnb.model.school.po.School;
import love.ytlsnb.model.school.po.StudentPhoto;
import love.ytlsnb.model.user.dto.*;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.model.user.po.UserInfo;
import love.ytlsnb.user.mapper.UserInfoMapper;
import love.ytlsnb.user.mapper.UserMapper;
import love.ytlsnb.user.service.UserInfoService;
import love.ytlsnb.user.service.UserService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户基本信息业务层实现类
 *
 * @author ula
 * @date 2024/01/21
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserProperties userProperties;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    @Lazy
    @Autowired
    private SchoolClient schoolClient;
    @Autowired
    private AliUtil aliUtil;


    /**
     * 根据传入参数新增用户数据，内部只会校验数据库内唯一的属性值是否已经被注册，不会校验参数合法性
     *
     * @param userInsertDTO 用户新增数据传输对象
     * @throws Exception 新增失败的异常
     */
    @Override
    @Transactional
    public void addUser(UserInsertDTO userInsertDTO) {
        // 校验是否可以新增用户数据
        // 校验身份证号
        String idNumber = userInsertDTO.getIdNumber();
        if (idNumber != null) {
            UserInfo selectOne = userInfoMapper.selectOne(new QueryWrapper<UserInfo>()
                    .eq(UserConstant.ID_NUMBER, idNumber));
            if (selectOne != null) {
                throw new BusinessException(ResultCodes.FORBIDDEN, "当前身份证号已注册过账号");
            }
        }
        // 校验手机号是否已经被注册
        String phone = userInsertDTO.getPhone();
        User selectOne = userMapper.selectOne(new QueryWrapper<User>()
                .eq(UserConstant.PHONE, phone));
        if (selectOne != null) {
            throw new BusinessException(ResultCodes.FORBIDDEN, "当前手机号已被注册");
        }
        User user = BeanUtil.copyProperties(userInsertDTO, User.class);
        UserInfo userInfo = BeanUtil.copyProperties(userInsertDTO, UserInfo.class);
        userInfoMapper.insert(userInfo);
        user.setUserInfoId(userInfo.getId());
        userMapper.insert(user);
    }

    /**
     * 根据传入账户名查找用户：账户可为手机号和身份证号（方法内部不再做参数合法性校验）
     *
     * @param account 代表用户的唯一字段（手机号/身份证号）
     * @return
     */
    @Override
    public User selectByAccount(String account) {
        if (account == null) {
            return null;
        } else {
            if (IdcardUtil.isValidCard(account)) {
                // 当前账户使用的身份证作为账号
                UserInfo userInfo = userInfoMapper.selectOne(new QueryWrapper<UserInfo>()
                        .eq(UserConstant.ID_NUMBER, account));
                if (userInfo == null) {
                    return null;
                }
                return userMapper.selectOne(new QueryWrapper<User>()
                        .eq(UserConstant.USER_INFO_ID, userInfo.getId()));
            } else {
                return userMapper.selectOne(new QueryWrapper<User>()
                        .eq(UserConstant.PHONE, account));
            }
        }
    }

    /*
     * 根据用户id返回脱敏后的用户信息
     *
     * @param id
     * @return
     */
    @Override
    public User selectInsensitiveUserById(Long id) {
        if (id == null) {
            return null;
        } else {
            User user = userMapper.selectById(id);
            if (user == null) {
                throw new BusinessException(ResultCodes.FORBIDDEN, "未能检测到您的用户信息");
            }
            // 敏感信息脱敏
            // 学号脱敏
            String afterStudentId = DesensitizedUtil.idCardNum(user.getStudentId(),
                    userProperties.getStudentIdParam1(),
                    userProperties.getStudentIdParam2());
            user.setStudentId(afterStudentId);
            // 密码脱敏
            user.setPassword(UserConstant.INSENSITIVE_PASSWORD);
            // 手机号脱敏
            user.setPhone(DesensitizedUtil.mobilePhone(user.getPhone()));
            // 积分脱敏
            user.setPoint(null);
            // 学校相关信息脱敏
            user.setIdentified(null);
            user.setSchoolId(null);
            user.setDeptId(null);
            user.setClazzId(null);
            // 用户数据库信息脱敏
            user.setCreateTime(null);
            user.setUpdateTime(null);
            user.setDeleted(null);

            return user;
        }
    }

    /**
     * 根据传入的数据传输对象中的非空属性进行用户查询
     *
     * @param userQueryDTO 用来查询的数据
     * @return 查询到的数据
     */
    @Override
    public List<User> list(UserQueryDTO userQueryDTO) {
        Map<String, Object> map = BeanUtil.beanToMap(userQueryDTO, true, true);

        // 特殊字段处理
        Object isIdentified = map.get(UserConstant.IS_IDENTIFIED_JAVA);
        if (isIdentified != null) {
            map.remove(UserConstant.IS_IDENTIFIED_JAVA);
            map.put(UserConstant.IS_IDENTIFIED, isIdentified);
        }
        return listByMap(map);
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO 封装用户的登录账户，密码
     * @param request      用户请求对象，用于获取用户的jwt令牌
     * @return
     */
    @Override
    public String login(UserLoginDTO userLoginDTO, HttpServletRequest request) {
        User user;
        // 校验传入参数
        if ((StrUtil.isBlankIfStr(userLoginDTO.getAccount()) || StrUtil.isBlankIfStr(userLoginDTO.getPassword()))) {
            // 非账户密码登录
            if ((StrUtil.isBlankIfStr(userLoginDTO.getPhone()) || StrUtil.isBlankIfStr(userLoginDTO.getCode()))) {
                // 手机号验证码也为空
                throw new BusinessException(ResultCodes.BAD_REQUEST, "登录请求参数错误");
            }
            //手机号验证码登录
            if (!PhoneUtil.isPhone(userLoginDTO.getPhone())) {
                throw new BusinessException(ResultCodes.BAD_REQUEST, "请填写正确的手机号");
            }
            String phoneCodeKey = RedisConstant.USER_PHONE_CODE_PREFIX + userLoginDTO.getPhone();
            String code = redisTemplate.opsForValue().get(phoneCodeKey);
            if (!userLoginDTO.getCode().equals(code)) {
                // 验证码不同
                throw new BusinessException(ResultCodes.UNAUTHORIZED, "验证码错误");
            }
            user = userMapper.selectOne(new QueryWrapper<User>()
                    .eq(UserConstant.PHONE, userLoginDTO.getPhone()));
            if (user == null) {
                // 查询用户数据为空
                throw new BusinessException(ResultCodes.UNAUTHORIZED, "当前手机号尚未注册账号");
            }
        } else {
            // 账户密码登录
            user = selectByAccount(userLoginDTO.getAccount());
            if (user == null) {
                // 用户不存在
                throw new BusinessException(ResultCodes.UNAUTHORIZED, "用户不存在");
            }
            // 用户存在，校验密码
            if (!BCrypt.checkpw(userLoginDTO.getPassword(), user.getPassword())) {
                // 密码错误
                throw new BusinessException(ResultCodes.UNAUTHORIZED, "密码错误");
            }
        }
        // 提前创建jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(UserConstant.USER_ID, user.getId());
        String jwt = JwtUtil.createJwt(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        log.info("生成的JWT令牌:{}", jwt);
        // jwt令牌中的签名，存储进redis用于进行用户账号的唯一登录
        String newSignature = jwt.substring(jwt.lastIndexOf('.') + 1);
        // 尝试登录
        String loginLockName = RedisConstant.USER_LOGIN_LOCK_PREFIX + user.getId();
        RLock lock = redissonClient.getLock(loginLockName);
        try {
            // 上锁，同一时间只允许一人进行登录操作
            boolean success = lock.tryLock();
            if (!success) {
                // 多人同时登录同一账号
                throw new BusinessException(ResultCodes.FORBIDDEN, "可能有其他用户正在登录您的账号，请检查您的账号");
            }
            redisTemplate.opsForValue()
                    .set(RedisConstant.USER_LOGIN_PREFIX + user.getId(),
                            newSignature,
                            jwtProperties.getUserTtl(),
                            TimeUnit.MILLISECONDS);
            return jwt;
        } finally {
            // 释放锁
            try {
                lock.unlock();
            } catch (Exception e) {
                log.error("释放锁失败:{},锁已经释放", loginLockName);
            }
        }
    }

    /**
     * 用户注册功能，新增用户和用户信息，内部会校验传入参数是否合法
     *
     * @param userRegisterDTO
     */
    @Override
    @Transactional
    public void register(UserRegisterDTO userRegisterDTO) {
        // 校验传入参数
        // 空参校验
        if (BeanUtil.hasNullField(userRegisterDTO)) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "用户注册信息不全");
        }
        // 数据长度校验
        if (!PhoneUtil.isPhone(userRegisterDTO.getPhone())) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "用户注册参数不合法");
        }

        // 校验验证码
        String phoneCodeKey = RedisConstant.USER_PHONE_CODE_PREFIX + userRegisterDTO.getPhone();
        String code = redisTemplate.opsForValue().get(phoneCodeKey);
        if (!userRegisterDTO.getCode().equals(code)) {
            throw new BusinessException(ResultCodes.UNAUTHORIZED, "验证码错误");
        }

        // 在数据库中查询当前用户数据
        User selectOne = userMapper.selectOne(new QueryWrapper<User>()
                .eq(UserConstant.PHONE, userRegisterDTO.getPhone()));
        // 用户已存在，报错
        if (selectOne != null) {
            throw new BusinessException(ResultCodes.FORBIDDEN, "用户已存在");
        }

        // 使用当前用户的手机号进行上锁
        String userRegisterKey = RedisConstant.USER_REGISTER_LOCK_PREFIX + userRegisterDTO.getPhone();
        RLock rLock = redissonClient.getLock(userRegisterKey);
        try {
            boolean success = rLock.tryLock();

            // 并发校验，有多个请求进行同一用户创建，抛异常
            if (!success) {
                throw new BusinessException(ResultCodes.FORBIDDEN, "用户已存在");
            }
            // 拿到锁后进行double check，避免并发时的冲突
            selectOne = userMapper.selectOne(new QueryWrapper<User>()
                    .eq(UserConstant.PHONE, userRegisterDTO.getPhone()));
            // 用户已存在，报错
            if (selectOne != null) {
                throw new BusinessException(ResultCodes.FORBIDDEN, "用户已存在");
            }
            // 新增用户数据
            User insertUser = new User(userRegisterDTO);

            insertUser.setNickname(UserConstant.DEFAULT_NICKNAME_PREFIX +
                    RandomUtil.randomString(UserConstant.DEFAULT_NICKNAME_LENGTH));

            // 默认属性，初始属性设置
            insertUser.setAvatar(UserConstant.DEFAULT_AVATAR);
            insertUser.setPoint(UserConstant.INITIAL_POINT);
            insertUser.setIdentified(UserConstant.UNIDENTIFIED);

            // 插入用户详情信息
            UserInfo insertUserInfo = new UserInfo();
            userInfoMapper.insert(insertUserInfo);
            log.info("插入用户详情:{}", insertUserInfo);

            insertUser.setUserInfoId(insertUserInfo.getId());
            userMapper.insert(insertUser);
            log.info("插入用户：{}", insertUser);
        } finally {
            //释放锁
            try {
                rLock.unlock();
            } catch (Exception e) {
                log.error("释放锁失败:{},锁已经释放", userRegisterKey);
            }
        }
    }

    @Override
    public boolean sign() {
        // 检查是否已经签到
        if (this.isSigned()) {
            // 已签到
            return false;
        }
        // 未签到过，进行签到
        User user = UserHolder.getUser();
        String signKey = RedisConstant.USER_SIGN_LOCK_PREFIX + user.getId();
        RLock signLock = redissonClient.getLock(signKey);
        try {
            boolean success = signLock.tryLock();
            if (!success) {
                throw new BusinessException(ResultCodes.FORBIDDEN, "请勿进行重复签到");
            }
            // 进行签到
            String signHistoryKey = RedisConstant.USER_SIGN_PREFIX +
                    LocalDate.now().format(DateTimeFormatter.ofPattern(RedisConstant.USER_SIGN_PERMOUTH_PATTERN)) +
                    user.getId();
            int dayOfMonth = LocalDate.now().getDayOfMonth();
            redisTemplate.opsForValue().setBit(signHistoryKey, dayOfMonth, true);
            return true;
        } finally {
            // 释放锁
            try {
                signLock.unlock();
            } catch (Exception e) {
                log.error("释放锁失败:{},锁已经释放", signKey);
            }
        }
    }

    /**
     * 用于获取用户今日的签到状态
     *
     * @return 用户的签到状态，true：已签到 false：未签到
     */
    @Override
    public boolean isSigned() {
        // 检查是否已经签到
        User user = UserHolder.getUser();
        String signHistoryKey = RedisConstant.USER_SIGN_PREFIX +
                LocalDate.now().format(DateTimeFormatter.ofPattern(RedisConstant.USER_SIGN_PERMOUTH_PATTERN)) +
                user.getId();
        int dayOfMonth = LocalDate.now().getDayOfMonth();
        Boolean sign = redisTemplate.opsForValue().getBit(signHistoryKey, dayOfMonth);
        return Boolean.TRUE.equals(sign);
    }

    /**
     * 向指定的电话号码发送验证码
     *
     * @param phone 指定的电话号
     * @throws Exception 发送验证码失败的异常
     */
    @Override
    public void sendShortMessage(String phone) throws Exception {
        // 校验手机号
        if (!PhoneUtil.isPhone(phone)) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "请输入正确的手机号");
        }
        // 生成存储验证码Key
        String phoneCodeKey = RedisConstant.USER_PHONE_CODE_PREFIX + phone;
        // 校验是否可以发送
        Long expire = redisTemplate.opsForValue().getOperations().getExpire(phoneCodeKey);
        if (expire != null && expire > userProperties.getPhoneCodeTtl() - userProperties.getResendCodeTimeInterval()) {
            throw new BusinessException(ResultCodes.FORBIDDEN, "请在一分钟后重试");
        }
        // 发送验证码
        String code = aliUtil.sendShortMessage(phone);
        // 存储验证码
        redisTemplate.opsForValue().set(phoneCodeKey, code, userProperties.getPhoneCodeTtl(), TimeUnit.MILLISECONDS);
    }


    /**
     * 根据传入的身份证图片识别身份证信息，同时为相关用户设置身份证相关信息
     *
     * @param idCardOss
     * @throws Exception
     */
    @Override
    public void uploadIdCard(String idCardOss) throws Exception {
        IdCard idCard = aliUtil.recognizeIdCardFront(idCardOss);
        User user = UserHolder.getUser();
        UserInfo userInfo = userInfoMapper.selectById(user.getUserInfoId());
        if (!idCard.getIdNumber().equals(userInfo.getIdNumber())) {
            // 校验身份证号是否一致
            throw new BusinessException(ResultCodes.NOT_ACCEPTABLE, "请上传本人身份证");
        }
        BeanUtil.copyProperties(idCard, userInfo);
        userInfoMapper.updateById(userInfo);
        log.info("修改UserInfo:{}", userInfo);
        // TODO 检查是否修改了updatetime属性
    }

    @Override
    public void uploadRealPhoto(String realPhoto) throws Exception {
        // 检测照片是否符合要求
        aliUtil.faceDetect(realPhoto);

        User user = UserHolder.getUser();
        Result<StudentPhoto> studentPhotoResult = schoolClient.getStudentPhoto(user.getId());
        if (studentPhotoResult.getCode() != ResultCodes.OK) {
            throw new BusinessException(studentPhotoResult.getCode(), studentPhotoResult.getMsg());
        }
        // 对比两张照片，不相似或者其他会抛异常
        aliUtil.faceCompare(studentPhotoResult.getData().getPhoto(), realPhoto);

        // 修改UserInfo
        UserInfo userInfo = userInfoMapper.selectById(user.getUserInfoId());
        userInfo.setRealPhoto(realPhoto);
        userInfoMapper.updateById(userInfo);
        log.info("修改UserInfo:{}", userInfo);
        // 设置用户状态为已认证态
        user.setIdentified(UserConstant.IDENTIFIED);
        userMapper.updateById(user);
        log.info("修改User:{}", user);
    }

    /**
     * 上传用户的录取通知书 TODO 目前内部未做任何校验，即可上传任意图片
     *
     * @param admissionLetter
     */
    @Override
    public void uploadAdmissionLetter(String admissionLetter) {
        User user = UserHolder.getUser();
        UserInfo userInfo = userInfoMapper.selectById(user.getUserInfoId());
        userInfo.setAdmissionLetterPhoto(admissionLetter);
        userInfoMapper.updateById(userInfo);
        log.info("修改UserInfo:{}", userInfo);
    }


    /*
    @Override
    public void addUser(UserInsertDTO userInsertDTO) throws Exception {
        // 参数合法性校验
        // 必填参数（手机号）为空
        if (StrUtil.isBlankIfStr(userInsertDTO.getPhone())) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "手机号不能为空");
        }
        // 手机号不合法
        if (!PhoneUtil.isPhone(userInsertDTO.getPhone())) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "请输入正确的手机号");
        }
        // 用户真实照片校验
        if (userInsertDTO.getRealPhoto() != null) {
            aliUtil.faceDetect(userInsertDTO.getRealPhoto());
        }
        if (userInsertDTO.getIdNumber() != null
                && !IdcardUtil.isValidCard(userInsertDTO.getIdNumber())) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "请输入正确的身份证号");
        }

        // 为用户添加学校相关属性
        Coladmin coladmin = AdminHolder.getAdmin();
        Long schoolId = coladmin.getSchoolId();
        School school = schoolMapper.selectById(schoolId);
        if (school == null) {
            throw new BusinessException(ResultCodes.SERVER_ERROR, "当前学校信息为空");
        }
        userInsertDTO.setSchoolId(schoolId);
        userInsertDTO.setSchoolName(school.getSchoolName());
        Result<User> userResult = userClient.addUser(userInsertDTO);
        if (userResult.getCode() != ResultCodes.OK) {
            // 远程调用失败
            throw new BusinessException(userResult.getCode(), userResult.getMsg());
        }
    }
*/
    @Override
    @Transactional
    public void addUserBatch(MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);

        Map<String, String> headerAliasMap = Map.of("学院", "deptName",
                "班级", "clazzName",
                "学号", "studentId",
                "姓名", "name",
                "身份证号", "idNumber",
                "手机号", "photo");
        reader.setHeaderAlias(headerAliasMap);
        List<UserInsertBatchDTO> userInsertBatchDTOList = reader.readAll(UserInsertBatchDTO.class);
        // 重复性校验集合
        Set<String> validateUserStudentIdSet = new HashSet<>();
        Set<String> validateUserIdNumberSet = new HashSet<>();
        Set<String> validateUserPhoneSet = new HashSet<>();

        // 获取学校信息
        Coladmin coladmin = ColadminHolder.getColadmin();
        Result<School> schoolResult = schoolClient.getSchoolById(coladmin.getSchoolId());
        if (schoolResult.getCode() != ResultCodes.OK) {
            throw new BusinessException(schoolResult.getCode(), schoolResult.getMsg());
        }
        School school = schoolResult.getData();
        Long schoolId = school.getId();

        // 获取学校中的所有学院数据
        Result<List<Dept>> listDeptBySchoolIdResult = schoolClient.listDeptBySchoolId(schoolId);
        if (listDeptBySchoolIdResult.getCode() != ResultCodes.OK) {
            throw new BusinessException(listDeptBySchoolIdResult.getCode(), listDeptBySchoolIdResult.getMsg());
        }
        List<Dept> deptList = listDeptBySchoolIdResult.getData();
        Map<String, Long> deptMap = deptList.stream().collect(Collectors.toMap(Dept::getDeptName, Dept::getId, (k1, k2) -> k1));
        // 获取学校中的所有班级数据
        Result<List<Clazz>> listClazzBySchoolIdResult = schoolClient.listClazzBySchoolId(schoolId);
        if (listClazzBySchoolIdResult.getCode() != ResultCodes.OK) {
            throw new BusinessException(listClazzBySchoolIdResult.getCode(), listClazzBySchoolIdResult.getMsg());
        }
        List<Clazz> clazzList = listClazzBySchoolIdResult.getData();

        Map<String, Long> clazzMap = clazzList.stream().collect(Collectors.toMap(Clazz::getClazzName, Clazz::getId, (k1, k2) -> k1));
        // 待新增的用户数据列表
        List<User> userList = new ArrayList<>();
        List<UserInfo> userInfoList = new ArrayList<>();

        // 批量设置待新增的数据(检查数据合法性)
        for (UserInsertBatchDTO userInsertBatchDTO : userInsertBatchDTOList) {
            String phone = userInsertBatchDTO.getPhone();
            String studentId = userInsertBatchDTO.getStudentId();
            String idNumber = userInsertBatchDTO.getIdNumber();
            String name = userInsertBatchDTO.getName();
            // 判断必填数据的合法性
            if (!PhoneUtil.isPhone(phone)
                    || !IdcardUtil.isValidCard(idNumber)
                    || StrUtil.isBlankIfStr(studentId)
                    || StrUtil.isBlankIfStr(name)) {
                throw new BusinessException(ResultCodes.BAD_REQUEST, "请核对必填数据");
            }
            if (validateUserPhoneSet.contains(phone)) {
                throw new BusinessException(ResultCodes.BAD_REQUEST, "存在重复手机号");
            }
            if (validateUserStudentIdSet.contains(studentId)) {
                throw new BusinessException(ResultCodes.BAD_REQUEST, "存在重复学号");
            }
            if (validateUserIdNumberSet.contains(idNumber)) {
                throw new BusinessException(ResultCodes.BAD_REQUEST, "存在重复身份证号");
            }
            validateUserPhoneSet.add(phone);
            validateUserStudentIdSet.add(studentId);
            validateUserIdNumberSet.add(idNumber);

            User user = new User();
            UserInfo userInfo = new UserInfo();

            String deptName = userInsertBatchDTO.getDeptName();
            if (deptName != null && !deptMap.containsKey(deptName)) {
                throw new BusinessException(ResultCodes.BAD_REQUEST,
                        "新增用户(身份证号: " + idNumber + " , 学号: " + studentId + " , 手机号: " + phone + ")的学院不存在");
            }
            String clazzName = userInsertBatchDTO.getClazzName();
            if (clazzName != null && !clazzMap.containsKey(clazzName)) {
                throw new BusinessException(ResultCodes.BAD_REQUEST,
                        "新增用户(身份证号: " + idNumber + " , 学号: " + studentId + " , 手机号: " + phone + ")的班级不存在");
            }

            user.setPhone(phone);
            user.setSchoolId(schoolId);
            if (!StrUtil.isBlankIfStr(deptName)) {
                user.setDeptId(deptMap.get(deptName));
            }
            if (!StrUtil.isBlankIfStr(deptName)) {
                user.setClazzId(clazzMap.get(clazzName));
            }

            userInfo.setName(userInsertBatchDTO.getName());
            userInfo.setIdNumber(userInsertBatchDTO.getIdNumber());
            userInfo.setSchoolName(school.getSchoolName());
            if (!StrUtil.isBlankIfStr(deptName)) {
                userInfo.setDeptName(deptName);
            }
            if (!StrUtil.isBlankIfStr(deptName)) {
                userInfo.setClazzName(clazzName);
            }

            userList.add(user);
            userInfoList.add(userInfo);
        }
        userInfoService.saveBatch(userInfoList);
        for (int i = 0; i < userList.size(); i++) {
            userList.get(i).setUserInfoId(userInfoList.get(i).getId());
        }
        saveBatch(userList);
    }
}
