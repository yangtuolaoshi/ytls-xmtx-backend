package love.ytlsnb.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.constants.RedisConstant;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.UserConstant;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.properties.JwtProperties;
import love.ytlsnb.common.properties.UserProperties;
import love.ytlsnb.common.utils.JwtUtil;
import love.ytlsnb.common.utils.UserHolder;
import love.ytlsnb.model.user.dto.UserQueryDTO;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.model.user.dto.UserLoginDTO;
import love.ytlsnb.model.user.dto.UserRegisterDTO;
import love.ytlsnb.model.user.po.UserInfo;
import love.ytlsnb.user.mapper.UserInfoMapper;
import love.ytlsnb.user.mapper.UserMapper;
import love.ytlsnb.user.service.UserService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户基本信息业务层实现类
 *
 * @author 金泓宇
 * @date 2024/01/21
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserProperties userProperties;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 根据传入账户名查找用户：账户可以使用户名或者手机号
     *
     * @param account
     * @return
     */
    @Override
    public User selectByAccount(String account) {
        if (account == null) {
            return null;
        } else {
            return userMapper.selectOne(new QueryWrapper<User>()
                    .eq(UserConstant.STUDENT_ID, account)
                    .or()
                    .eq(UserConstant.PHONE, account));
        }
    }

    /**
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
     * 用户注册功能，新增用户和用户信息
     *
     * @param userRegisterDTO
     */
    @Override
    @Transactional
    public void register(UserRegisterDTO userRegisterDTO) {
        // 校验传入参数
        // 空参校验
        if (StrUtil.isBlankIfStr(userRegisterDTO.getStudentId()) ||
                StrUtil.isBlankIfStr(userRegisterDTO.getPhone())) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "用户注册信息不全");
        }
        // 数据长度校验
        if (userRegisterDTO.getStudentId().length() > UserConstant.STUDENT_ID_MAX_LENGTH ||
                userRegisterDTO.getNickname().length() > UserConstant.NICKNAME_MAX_LENGTH ||
                userRegisterDTO.getPassword().length() > UserConstant.PASSWORD_MAX_LENGTH ||
                userRegisterDTO.getPhone().length() > UserConstant.PHONE_MAX_LENGTH) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "用户注册参数不合法");
        }

        // TODO 验证码校验

        // 在数据库中查询当前用户数据
        User selectOne = userMapper.selectOne(new QueryWrapper<User>()
                .eq(UserConstant.PHONE, userRegisterDTO.getPhone())
                .or()
                .eq(UserConstant.STUDENT_ID, userRegisterDTO.getStudentId()));
        // 用户已存在，报错
        if (selectOne != null) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "用户已存在");
        }

        // 使用当前学生的学号进行上锁
        String userRegisterKey = RedisConstant.USER_REGISTER_LOCK_PREFIX + userRegisterDTO.getStudentId();
        RLock rLock = redissonClient.getLock(userRegisterKey);
        try {
            boolean success = rLock.tryLock();

            // 并发校验，有多个请求进行同一用户创建，抛异常
            if (!success) {
                throw new BusinessException(ResultCodes.BAD_REQUEST, "用户已存在");
            }
            // 拿到锁后进行double check，避免并发时的冲突
            selectOne = userMapper.selectOne(new QueryWrapper<User>()
                    .eq(UserConstant.PHONE, userRegisterDTO.getPhone())
                    .or()
                    .eq(UserConstant.STUDENT_ID, userRegisterDTO.getStudentId()));
            // 用户已存在，报错
            if (selectOne != null) {
                throw new BusinessException(ResultCodes.BAD_REQUEST, "用户已存在");
            }

            // 新增用户数据
            User insertUser = new User(userRegisterDTO);

            if (insertUser.getNickname() == null) {
                insertUser.setNickname(UserConstant.DEFAULT_NICKNAME_PREFIX +
                        RandomUtil.randomString(UserConstant.DEFAULT_NICKNAME_LENGTH));
            }

            // 密码加盐加密
            String password = insertUser.getPassword();
            String hashpw = BCrypt.hashpw(password);
            insertUser.setPassword(hashpw);

            // 默认属性，初始属性设置
            insertUser.setGender(UserConstant.DEFAULT_GENDER);
            insertUser.setAvatar(UserConstant.DEFAULT_AVATAR);
            insertUser.setPoint(UserConstant.INITIAL_POINT);
            insertUser.setIdentified(UserConstant.UNIDENTIFIED);

            userMapper.insert(insertUser);
            log.info("插入用户：{}", insertUser);

            // 插入用户详情信息
            UserInfo insertUserInfo = new UserInfo();
            insertUserInfo.setUserId(insertUser.getId());
            userInfoMapper.insert(insertUserInfo);
            log.info("插入用户详情:{}", insertUserInfo);
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
     * 用户登录
     *
     * @param userLoginDTO 封装用户的登录账户，密码
     * @param request      用户请求对象，用于获取用户的jwt令牌
     * @return
     */
    @Override
    public String login(UserLoginDTO userLoginDTO, HttpServletRequest request) {
        // 校验传入参数
        if (StrUtil.isBlankIfStr(userLoginDTO.getAccount()) || StrUtil.isBlankIfStr(userLoginDTO.getPassword())) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "用户登录信息不全");
        }
        User user = selectByAccount(userLoginDTO.getAccount());

        // 用户不存在
        if (user == null) {
            throw new BusinessException(ResultCodes.UNAUTHORIZED, "用户不存在");
        }

        // 用户存在，提前创建jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(UserConstant.USER_ID, user.getId());
        String jwt = JwtUtil.createJwt(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        log.info("生成的JWT令牌：{}", jwt);
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
}
