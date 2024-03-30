package love.ytlsnb.user.utils;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.constants.RedisConstant;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.UserConstant;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.properties.JwtProperties;
import love.ytlsnb.common.utils.JwtUtil;
import love.ytlsnb.model.user.po.User;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ula
 * @date 2024/3/13 20:58
 */
@Slf4j
@Component
public class UserLoginUtil {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private StringRedisTemplate redisTemplate;

    public String tryLogin(User user) {
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
}
