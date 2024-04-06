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
     * 用户生成jwt的密钥，最低32位
     */
    private String userSecretKey;
    /**
     * 用户jwt令牌有效期
     */
    private long userTtl;
    /**
     * 用户jwt在header中的参数KEY
     */
    private String userTokenName;

    /**
     * 学校管理员账号生成jwt的密钥，最低32位
     */
    private String coladminSecretKey;
    /**
     * 学校管理员账号的jwt令牌有效期
     */
    private long coladminTtl;
    /**
     * 学校管理员账号的jwt在header中的参数KEY
     */
    private String coladminTokenName;
}
