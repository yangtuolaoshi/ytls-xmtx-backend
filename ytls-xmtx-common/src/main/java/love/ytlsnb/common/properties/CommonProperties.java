package love.ytlsnb.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author ula
 * @date 2024/3/10 9:47
 */
@Data
@Component
@RefreshScope// 必须加，从配置中心中获取配置
@ConfigurationProperties("xmtx.common")
public class CommonProperties {
    /**
     * 手机验证码的有效时间（毫秒）
     */
    private Long phoneCodeTtl;
    /**
     * 重新发送验证码的间隔时间（毫秒）
     */
    private Long resendCodeTimeInterval;
}
