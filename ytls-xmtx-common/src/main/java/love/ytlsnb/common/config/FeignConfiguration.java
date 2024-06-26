package love.ytlsnb.common.config;

import cn.hutool.core.util.StrUtil;
import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import love.ytlsnb.common.properties.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;

/**
 * @author ula
 * @date 2024/3/7 20:02
 */
@Configuration
public class FeignConfiguration {
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * Feign
     *
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // TODO 无法将coladmin正常注入jwtProperties，目前只能写死
            //1、从RequestContextHolder获取原始请求的请求数据(请求参数、请求头等)
            ServletRequestAttributes attributes;
            try {
                attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            } catch (IllegalStateException ie) {
                return;
            }
            HttpServletRequest request = attributes.getRequest();
            //2、同步请求头数据
//            String coladminTokenName = jwtProperties.getColadminTokenName();
            String coladminTokenName = "Authentication-Coladmin";
            String userTokenName = jwtProperties.getUserTokenName();

            String coladminTokenValue = request.getHeader(coladminTokenName);
            if (!StrUtil.isBlankIfStr(coladminTokenValue)) {
                requestTemplate.header(coladminTokenName, coladminTokenValue);
            }

            String userTokenValue = request.getHeader(userTokenName);
            if (!StrUtil.isBlankIfStr(userTokenValue)) {
                requestTemplate.header(userTokenName, userTokenValue);
            }
        };
    }
}
