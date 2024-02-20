package love.ytlsnb.school.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import love.ytls.api.quest.QuestClient;
import love.ytls.api.user.UserClient;
import love.ytlsnb.common.constants.RedisConstant;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.SchoolConstant;
import love.ytlsnb.common.constants.UserConstant;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.properties.JwtProperties;
import love.ytlsnb.common.utils.AdminHolder;
import love.ytlsnb.common.utils.AliUtil;
import love.ytlsnb.common.utils.JwtUtil;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.school.dto.AdminLoginDTO;
import love.ytlsnb.model.school.dto.AdminRegisterDTO;
import love.ytlsnb.model.school.po.Admin;
import love.ytlsnb.model.school.po.School;
import love.ytlsnb.model.user.dto.UserInsertBatchDTO;
import love.ytlsnb.model.user.dto.UserInsertDTO;
import love.ytlsnb.model.user.dto.UserLoginDTO;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.model.user.po.UserInfo;
import love.ytlsnb.school.mapper.AdminMapper;
import love.ytlsnb.school.mapper.SchoolMapper;
import love.ytlsnb.school.service.AdminService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author ula
 * @date 2024/2/6 16:28
 */
@Slf4j
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private SchoolMapper schoolMapper;
    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AliUtil aliUtil;

    /**
     * 学校管理人员登录
     *
     * @param adminLoginDTO 封装学校管理人员的登录账户，密码
     * @param request       用户请求对象，用于获取用户的jwt令牌
     * @return 登录成功的JWT令牌
     */
    @Override
    public String login(AdminLoginDTO adminLoginDTO, HttpServletRequest request) {
        // 校验传入参数
        if (StrUtil.isBlankIfStr(adminLoginDTO.getUsername()) || StrUtil.isBlankIfStr(adminLoginDTO.getPassword())) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "登录账户登录信息不全");
        }
        Admin admin = this.getOne(new QueryWrapper<Admin>().eq(SchoolConstant.USERNAME, adminLoginDTO.getUsername()));

        // 账号不存在
        if (admin == null) {
            throw new BusinessException(ResultCodes.UNAUTHORIZED, "账号不存在");
        }

        // 账户存在，校验密码
        if (!BCrypt.checkpw(adminLoginDTO.getPassword(), admin.getPassword())) {
            // 密码错误
            throw new BusinessException(ResultCodes.UNAUTHORIZED, "密码错误");
        }
        // 提前创建jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(SchoolConstant.ADMIN_ID, admin.getId());
        String jwt = JwtUtil.createJwt(jwtProperties.getAdminSecretKey(), jwtProperties.getAdminTtl(), claims);
        log.info("生成的JWT令牌：{}", jwt);
        // jwt令牌中的签名,用来做唯一登录校验
        String newSignature = jwt.substring(jwt.lastIndexOf('.') + 1);

        // 登录
        String adminLoginLockKey = RedisConstant.ADMIN_LOGIN_LOCK_PREFIX + admin.getId();
        RLock lock = redissonClient.getLock(adminLoginLockKey);

        try {
            // 上锁，同一时间只允许管理员账号在一处地方进行登录操作
            boolean success = lock.tryLock();
            if (!success) {
                // 多人同时登录同一账号
                throw new BusinessException(ResultCodes.FORBIDDEN, "多人登录同一管理员账号");
            }
            // 登录，记录当前登录对应的签名
            redisTemplate.opsForValue().set(RedisConstant.ADMIN_LOGIN_PREFIX + admin.getId(), newSignature, jwtProperties.getAdminTtl(), TimeUnit.MILLISECONDS);
            return jwt;

        } finally {
            // 释放锁
            try {
                lock.unlock();
            } catch (Exception e) {
                log.error("释放锁失败:{},锁已经释放", lock);
            }
        }
    }

    /**
     * 根据传入管理员ID查找并返回脱敏后的管理员信息
     *
     * @param adminId 待查找的管理员ID
     * @return 脱敏后的管理员信息
     */
    @Override
    public Admin selectInsensitiveAdminById(Long adminId) {
        if (adminId == null) {
            throw new NullPointerException("传入管理员账号ID为空");
        }
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq(SchoolConstant.ID, adminId).eq(SchoolConstant.STATUS, SchoolConstant.ENABLED));

        if (admin == null) {
            throw new BusinessException(ResultCodes.UNAUTHORIZED, "管理员账号不存在/状态异常");
        }

        admin.setPassword(SchoolConstant.INSENSITIVE_PASSWORD);
        admin.setCreateTime(null);
        admin.setUpdateTime(null);
        admin.setDeleted(null);
        return admin;
    }

    @Override
    @Transactional
    public void register(AdminRegisterDTO adminRegisterDTO) {
        // 校验参数
        if (BeanUtil.hasNullField(adminRegisterDTO)) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "传入参数不完整");
        }

        // 用户名校验
        String username = adminRegisterDTO.getUsername();
        Admin selectOne = adminMapper.selectOne(new QueryWrapper<Admin>().eq(SchoolConstant.USERNAME, username));
        if (selectOne != null) {
            throw new BusinessException(ResultCodes.FORBIDDEN, "账号名称已存在");
        }

        // 对写入操作进行上锁
        String adminRegisterLockKey = RedisConstant.ADMIN_REGISTER_LOCK_PREFIX + adminRegisterDTO.getUsername();
        RLock lock = redissonClient.getLock(adminRegisterLockKey);
        try {
            boolean success = lock.tryLock();
            if (!success) {
                // 多人使用同一用户名进行注册
                throw new BusinessException(ResultCodes.FORBIDDEN, "账号名称已存在");
            }

            // 获取锁成功，进行double check
            selectOne = adminMapper.selectOne(new QueryWrapper<Admin>().eq(SchoolConstant.USERNAME, username));
            if (selectOne != null) {
                throw new BusinessException(ResultCodes.FORBIDDEN, "账号名称已存在");
            }

            Admin admin = new Admin();
            BeanUtil.copyProperties(adminRegisterDTO, admin);

            // 密码加盐加密
            admin.setPassword(BCrypt.hashpw(admin.getPassword()));
            // 设置属性初始值
            admin.setStatus(SchoolConstant.DISABLED);
            adminMapper.insert(admin);
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                log.error("释放锁失败:{},锁已经释放", adminRegisterLockKey);
            }
        }
    }

    /**
     * 根据传入的用户新增数据传输对象新增用户数据，内部会检查数据合法性
     *
     * @param userInsertDTO 用户新增数据传输对象
     */
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
        Admin admin = AdminHolder.getAdmin();
        Long schoolId = admin.getSchoolId();
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

    @Override
    public void addUserBatch(MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 后续使用properties修改 TODO
        HashMap<String, String> headerAliasMap = new HashMap<>();
        headerAliasMap.put("学号", "studentId");
        headerAliasMap.put("身份证号", "idNumber");
        headerAliasMap.put("姓名", "name");
        headerAliasMap.put("手机号", "photo");

        reader.setHeaderAlias(headerAliasMap);
        List<UserInsertBatchDTO> userInsertBatchDTOList = reader.readAll(UserInsertBatchDTO.class);
        // 重复性校验
        Set<String> validateUserPhoneSet = new HashSet<>();
        Set<String> validateUserStudentIdSet = new HashSet<>();

        Admin admin = AdminHolder.getAdmin();
        School school = schoolMapper.selectById(admin.getSchoolId());
        List<User> userList = new ArrayList<>();
        for (UserInsertBatchDTO userInsertBatchDTO : userInsertBatchDTOList) {
            String phone = userInsertBatchDTO.getPhone();
            String studentId = userInsertBatchDTO.getStudentId();
            if (validateUserPhoneSet.contains(phone)) {
                throw new BusinessException(ResultCodes.BAD_REQUEST, "存在重复手机号");
            }
            if (validateUserStudentIdSet.contains(studentId)) {
                throw new BusinessException(ResultCodes.BAD_REQUEST, "存在重复学号");
            }
            validateUserPhoneSet.add(phone);
            validateUserStudentIdSet.add(studentId);

            User user = new User();
            UserInfo userInfo = new UserInfo();
            user.setPhone(phone);
            user.setSchoolId(school.getId());
            userInfo.setSchoolName(school.getSchoolName());
            // TODO
        }
    }
}
