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
@ConfigurationProperties("xmtx.cache")
public class CacheProperties {
    /**
     * 空值在redis中的存储时长（毫秒）
     */
    private Long nullTtl;
}
