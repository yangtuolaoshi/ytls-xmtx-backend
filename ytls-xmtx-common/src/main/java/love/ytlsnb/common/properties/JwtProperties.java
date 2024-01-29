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
@ConfigurationProperties("xmtx.jwt")
public class JwtProperties {
    /**
     * 用户端生成jwt令牌相关配置
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;
}
