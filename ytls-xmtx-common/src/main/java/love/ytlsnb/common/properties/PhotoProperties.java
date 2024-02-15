package love.ytlsnb.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ula
 * @date 2024/2/14 16:49
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "xmtx.photo")
public class PhotoProperties {
    /**
     * 上传图片后转存的最大值
     */
    private Float maxSize;
    /**
     * 支持的图片类型
     */
    private List<String> supportedTypes;
}
