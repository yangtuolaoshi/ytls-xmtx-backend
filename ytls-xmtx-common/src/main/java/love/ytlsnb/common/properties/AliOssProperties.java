package love.ytlsnb.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author ula
 * @date 2024/2/13 9:59
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "xmtx.alioss")
public class AliOssProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

}
