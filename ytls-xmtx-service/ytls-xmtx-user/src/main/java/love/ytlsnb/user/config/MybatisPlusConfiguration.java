package love.ytlsnb.user.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import love.ytlsnb.user.handler.MybatisPlusMetaObjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ula
 * @date 2024/1/31 17:55
 */
@Configuration
public class MybatisPlusConfiguration {
    @Bean
    public MetaObjectHandler metaObjectHandler(){
        return new MybatisPlusMetaObjectHandler();
    }
}
