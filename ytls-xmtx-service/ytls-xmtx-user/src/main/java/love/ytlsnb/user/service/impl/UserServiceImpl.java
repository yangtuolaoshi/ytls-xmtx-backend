package love.ytlsnb.user.service.impl;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.constants.MessageConstant;
import love.ytlsnb.common.constants.RedisConstant;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.UserConstant;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.properties.JwtProperties;
import love.ytlsnb.common.properties.UserProperties;
import love.ytlsnb.common.utils.JwtUtil;
import love.ytlsnb.model.user.pojo.User;
import love.ytlsnb.model.user.pojo.dto.UserLoginDTO;
import love.ytlsnb.model.user.pojo.dto.UserRegisterDTO;
import love.ytlsnb.user.mapper.UserMapper;
import love.ytlsnb.user.service.UserService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 用户基本信息业务层实现类
 *
 * @author 金泓宇
 * @date 2024/01/21
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
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

    @Override
    public User selectById(Long id) {
        if (id == null) {
            return null;
        } else {
            return userMapper.selectById(id);
        }
    }

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
            user.setPassword("********");
            // 手机号脱敏
            user.setPhone(DesensitizedUtil.mobilePhone(user.getPhone()));
            // 积分脱敏
            user.setPoint(null);
            // 学校相关信息脱敏
            user.setIdentified(null);
            user.setSchoolId(null);
            user.setDeptId(null);
            user.setClassId(null);
            // 用户数据库信息脱敏
            user.setCreateTime(null);
            user.setUpdateTime(null);
            user.setDeleted(null);

            return user;
        }
    }


    /**
     * 用户注册
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
        RLock rLock = redissonClient.getLock(RedisConstant.USER_REGISTER_LOCK_PREFIX + userRegisterDTO.getStudentId());
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

        try {
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
            insertUser.setPoint(UserConstant.DEFAULT_POINT);
            insertUser.setIdentified(UserConstant.UNIDENTIFIED);
            insertUser.setCreateTime(LocalDateTime.now());
            insertUser.setUpdateTime(LocalDateTime.now());
            insertUser.setDeleted(UserConstant.UNDELETED);

            log.info("插入用户：{}", insertUser);
            userMapper.insert(insertUser);

        } finally {
            //释放锁
            rLock.unlock();
        }
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
        // jwt令牌中的签名，用来验证重复登录时是否是同一用户登录
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
            // redis中的签名
            String loginedSignature = redisTemplate.opsForValue().get(RedisConstant.USER_LOGIN_PREFIX + user.getId());
            if (StrUtil.isBlankIfStr(loginedSignature)) {
                // redis中没有数据，用户未登录
                redisTemplate.opsForValue()
                        .set(RedisConstant.USER_LOGIN_PREFIX + user.getId(),
                                newSignature,
                                jwtProperties.getUserTtl(),
                                TimeUnit.MILLISECONDS);
                return jwt;
            } else {
                // redis中有数据，用户已登录
                // 获取请求头中的jwt令牌
                String token = request.getHeader(jwtProperties.getUserTokenName());
                if (StrUtil.isBlankIfStr(token)) {
                    // 当前请求的用户是第一次进行登录操作，但需要登录的账号已经被登录
                    throw new BusinessException(ResultCodes.FORBIDDEN, "账户已登录");
                }
                // 当前请求的用户进行重复登录操作，验证需要登录的账号和用户已经登录过的账号是否相同
                String requestSignature = token.substring(token.lastIndexOf('.') + 1);
                if (requestSignature.equals(loginedSignature)) {
                    // 同一用户进行重新登陆，刷新ttl并重新下发jwt
                    redisTemplate.opsForValue()
                            .set(RedisConstant.USER_LOGIN_PREFIX + user.getId(),
                                    newSignature,
                                    jwtProperties.getUserTtl(),
                                    TimeUnit.MILLISECONDS);
                    return jwt;
                } else {
                    // 需要登录的账号和用户已经登录过的账号不相同
                    throw new BusinessException(ResultCodes.FORBIDDEN, "账户已登录");
                }
            }
        } finally {
            // 释放锁
            lock.unlock();
        }
    }
}
