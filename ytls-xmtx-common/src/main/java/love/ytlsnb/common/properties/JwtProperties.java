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
     * 生成jwt的密钥，最低32位
     */
    private String userSecretKey;
    /**
     * jwt令牌有效期
     */
    private long userTtl;
    /**
     * jwt在header中的参数KEY
     */
    private String userTokenName;
}
