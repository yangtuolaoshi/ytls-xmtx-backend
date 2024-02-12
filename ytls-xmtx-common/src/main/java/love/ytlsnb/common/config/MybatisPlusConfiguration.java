package love.ytlsnb.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
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
}
