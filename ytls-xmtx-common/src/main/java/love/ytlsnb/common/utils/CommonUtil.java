package love.ytlsnb.common.utils;

import cn.hutool.core.util.PhoneUtil;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.constants.RedisConstant;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.properties.CommonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author ula
 * @date 2024/3/10 9:44
 */
@Slf4j
@Component
public class CommonUtil {
    @Autowired
    private CommonProperties commonProperties;
    @Autowired
    private AliUtil aliUtil;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 向指定的电话号码发送验证码
     *
     * @param phone 指定的电话号
     * @throws Exception 发送验证码失败的异常
     */
    public void sendShortMessage(String phone) throws Exception {
        // 校验手机号
        if (!PhoneUtil.isPhone(phone)) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "请输入正确的手机号");
        }
        // 生成存储验证码Key
        String phoneCodeKey = RedisConstant.PHONE_CODE_PREFIX + phone;
        // 校验是否可以发送
        Long expire = redisTemplate.opsForValue().getOperations().getExpire(phoneCodeKey);
        if (expire != null && expire > commonProperties.getPhoneCodeTtl() - commonProperties.getResendCodeTimeInterval()) {
            throw new BusinessException(ResultCodes.FORBIDDEN, "请在一分钟后重试");
        }
        // 发送验证码
        String code = aliUtil.sendShortMessage(phone);
        // 存储验证码
        redisTemplate.opsForValue().set(phoneCodeKey, code, commonProperties.getPhoneCodeTtl(), TimeUnit.MILLISECONDS);
    }

    /**
     * 根据传入手机号在Redis中查询相关验证码
     *
     * @param phone 待查询验证码的手机号（内部不做校验）
     * @param code  待校验的验证码（内部不做校验，传入不能为空）
     * @return 查询到的验证码（可能为null）
     */
    public Boolean checkShortMessage(String phone, String code) {
        String phoneCodeKey = RedisConstant.PHONE_CODE_PREFIX + phone;
        String phoneCode = redisTemplate.opsForValue().get(phoneCodeKey);
        if (!code.equals(phoneCode)) {
            return false;
        } else {
            // 验证码已使用，删除
            redisTemplate.delete(phoneCodeKey);
            return true;
        }
    }
}
