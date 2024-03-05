package love.ytlsnb.user.config;

import love.ytlsnb.common.constants.UserConstant;
import love.ytlsnb.common.json.JacksonObjectMapper;
import love.ytlsnb.user.intercepter.UserHolderIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author ula
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Autowired
    UserHolderIntercepter userHolderIntercepter;

    /**
     * 注册用户态保存拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userHolderIntercepter)
                .addPathPatterns(UserConstant.USER_ALL_URL);
    }

    /**
     * 处理 Bean 对象与 JSON的转换，会添加一个转换器，可以处理对象中 LocalDateTime这一类消息的转换
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建消息转换器
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //创建并设置自定义对象转换器
        converter.setObjectMapper(new JacksonObjectMapper());
        //将自定义消息转换器添加进框架中
        converters.add(0, converter);
    }
}
