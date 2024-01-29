package love.ytlsnb.user.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.constants.MessageConstant;
import love.ytlsnb.common.constants.RedisConstant;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.UserConstant;
import love.ytlsnb.common.exception.UserLoginException;
import love.ytlsnb.common.properties.JwtProperties;
import love.ytlsnb.common.utils.JwtUtil;
import love.ytlsnb.model.user.pojo.User;
import love.ytlsnb.model.user.pojo.dto.UserRegisterDTO;
import love.ytlsnb.user.mapper.UserMapper;
import love.ytlsnb.user.service.UserService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    private RedissonClient redissonClient;
    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    @Transactional
    public String register(UserRegisterDTO userRegisterDTO) {
        // 校验传入参数
        // 空参校验
        if (StrUtil.isBlankIfStr(userRegisterDTO.getStudentId()) ||
                StrUtil.isBlankIfStr(userRegisterDTO.getPhone())) {
            throw new UserLoginException(ResultCodes.BAD_REQUEST, MessageConstant.USER_LOGIN_INFO_INCOMPLETE);
        }
        // 数据长度校验
        if (userRegisterDTO.getStudentId().length() > 16 ||
                userRegisterDTO.getNickname().length() > 32 ||
                userRegisterDTO.getPassword().length() > 32 ||
                userRegisterDTO.getPhone().length() > 11) {
            throw new UserLoginException(ResultCodes.BAD_REQUEST, MessageConstant.USER_LOGIN_INFO_ERROR);
        }

        // TODO 验证码校验

        // 在数据库中查询当前用户数据
        User selectOne = userMapper.selectOne(new QueryWrapper<User>()
                .eq(UserConstant.PHONE, userRegisterDTO.getPhone())
                .or()
                .eq(UserConstant.STUDENT_ID, userRegisterDTO.getStudentId()));
        // 用户已存在，报错
        if (selectOne != null) {
            throw new UserLoginException(ResultCodes.BAD_REQUEST, MessageConstant.USER_ALREADY_EXISTS);
        }

        // 使用当前学生的学号进行上锁
        RLock rLock = redissonClient.getLock(RedisConstant.USER_REGISTER_LOCK_PREFIX + userRegisterDTO.getStudentId());
        boolean success = rLock.tryLock();

        // 并发校验，有多个请求进行同一用户创建，抛异常
        if (!success) {
            throw new UserLoginException(ResultCodes.BAD_REQUEST, MessageConstant.USER_ALREADY_EXISTS);
        }

        // 拿到锁后进行double check，避免并发时的冲突
        selectOne = userMapper.selectOne(new QueryWrapper<User>()
                .eq(UserConstant.PHONE, userRegisterDTO.getPhone())
                .or()
                .eq(UserConstant.STUDENT_ID, userRegisterDTO.getStudentId()));
        // 用户已存在，报错
        if (selectOne != null) {
            throw new UserLoginException(ResultCodes.BAD_REQUEST, MessageConstant.USER_ALREADY_EXISTS);
        }

        try {
            User insertUser = new User(userRegisterDTO);

            if (insertUser.getNickname() == null) {
                insertUser.setNickname(UserConstant.DEFAULT_NICKNAME_PREFIX + RandomUtil.randomString(UserConstant.DEFAULT_NICKNAME_LENGTH));
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

            // 生成jwt令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put(UserConstant.USER_ID, insertUser.getId());
            String jwt = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

            return jwt;
        } finally {
            //释放锁
            rLock.unlock();
        }
    }
}
