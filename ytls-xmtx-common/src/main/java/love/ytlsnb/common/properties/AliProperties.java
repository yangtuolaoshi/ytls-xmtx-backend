package love.ytlsnb.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author ula
 * @date 2024/2/13 16:23
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "xmtx.ali")
public class AliProperties {
    /**
     * 阿里云用户的鉴权ID
     */
    private String accessKeyId;
    /**
     * 阿里云用户的鉴权密钥
     */
    private String accessKeySecret;
}
