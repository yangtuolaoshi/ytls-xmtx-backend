package love.ytlsnb.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author ula
 * @date 2024/2/15 11:24
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties("xmtx.ali.ocr")
public class AliOcrProperties {
    private String endpoint;

}
