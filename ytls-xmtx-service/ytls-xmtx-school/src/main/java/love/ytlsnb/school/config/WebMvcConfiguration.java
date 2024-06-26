package love.ytlsnb.school.config;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.constants.SchoolConstant;
import love.ytlsnb.common.json.JacksonObjectMapper;
import love.ytlsnb.school.intercepter.HolderIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author ula
 * @date 2024/2/7 14:53
 */
@Slf4j
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Autowired
    HolderIntercepter holderIntercepter;

    /**
     * 学校管理端账户保存拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(holderIntercepter)
                .addPathPatterns(SchoolConstant.COLADMIN_ALL_URL)
                .addPathPatterns(SchoolConstant.SCHOOL_ALL_URL)
                .addPathPatterns(SchoolConstant.QUEST_ALL_URL)
                .addPathPatterns(SchoolConstant.USER_ALL_URL)
                .addPathPatterns(SchoolConstant.SCHEDULE_ALL_URL)
                .addPathPatterns("/coladmin/**")
                .addPathPatterns("/clazz/**")
                .addPathPatterns("/dept/**")
                .excludePathPatterns(SchoolConstant.COLADMIN_LOGIN_URL);
    }
    /**
     * 处理 Bean 对象与 JSON的转换，会添加一个转换器，可以处理对象中 LocalDateTime这一类消息的转换
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("正在添加自定义JSON转换器");
        //创建消息转换器
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //创建并设置自定义对象转换器
        converter.setObjectMapper(new JacksonObjectMapper());
        //将自定义消息转换器添加进框架中
        converters.add(0, converter);
    }
}
