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
@ConfigurationProperties(prefix = "xmtx.ali.oss")
public class AliOssProperties {
    /**
     * 阿里云OSS的域名
     */
    private String endpoint;
    /**
     * OSS中存储数据的bucketName
     */
    private String bucketName;
}
