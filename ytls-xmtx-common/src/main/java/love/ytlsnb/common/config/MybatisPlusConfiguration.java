package love.ytlsnb.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import love.ytlsnb.common.handler.MybatisPlusMetaObjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ula
 * @date 2024/2/9 11:02
 */
@Configuration
public class MybatisPlusConfiguration {
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MybatisPlusMetaObjectHandler();
    }
    @Bean
    public MybatisPlusInterceptor mybatisPlusPaginationInterceptor() {
        MybatisPlusInterceptor mpInterceptor = new MybatisPlusInterceptor();
        mpInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));// 分页拦截器
        return mpInterceptor;
    }
}
