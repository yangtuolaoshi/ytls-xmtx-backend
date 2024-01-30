package love.ytlsnb.user.config;

import love.ytlsnb.user.intercepter.UserHolderIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ula
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Autowired
    UserHolderIntercepter userHolderIntercepter;

    /**
     * 注册用户态保存拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userHolderIntercepter)
                .addPathPatterns("/api/user/**")
                .excludePathPatterns("/api/user/register")
                .excludePathPatterns("/api/user/login");
    }
}
