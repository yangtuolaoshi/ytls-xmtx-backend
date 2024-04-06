package love.ytlsnb.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author ula
 * @date 2024/2/13 14:19
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "xmtx.ali.sms")
public class AliSmsProperties {
    /**
     * 阿里云SMS的域名
     */
    private String endpoint;
    /**
     * 短信声明：类似一个标题
     */
    private String signName;
    /**
     * 短信模板号
     */
    private String templateCode;
    /**
     * 验证码长度
     */
    private Integer codeLength;
}
