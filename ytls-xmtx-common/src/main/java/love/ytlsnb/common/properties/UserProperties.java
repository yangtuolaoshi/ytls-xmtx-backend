package love.ytlsnb.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author ula
 */
@Data
@Component
@RefreshScope// 必须加，从配置中心中获取配置
@ConfigurationProperties("xmtx.user")
public class UserProperties {
    /**
     * 用户学号脱敏相关参数：
     * param1：学号保留前几位
     * param2：学号保留后几位
     */
    private Integer studentIdParam1;
    private Integer studentIdParam2;
    /**
     * 用户手机验证码的有效时间（毫秒）
     */
    private Long phoneCodeTtl;
    /**
     * 用户重新发送验证码的间隔时间（毫秒）
     */
    private Long resendCodeTimeInterval;
}
