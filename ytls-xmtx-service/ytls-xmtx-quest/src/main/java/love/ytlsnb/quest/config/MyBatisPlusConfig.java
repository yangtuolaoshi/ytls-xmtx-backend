package love.ytlsnb.quest.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatisPlus配置类
 *
 * @author 金泓宇
 * @date 2024/3/1
 */
@Configuration
public class MyBatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mpInterceptor() {
        MybatisPlusInterceptor mpInterceptor = new MybatisPlusInterceptor();
        mpInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());// 分页拦截器
        return mpInterceptor;
    }

    @Bean
    public CustomSqlInjector customSqlInjector() {
        return new CustomSqlInjector();
    }
}
