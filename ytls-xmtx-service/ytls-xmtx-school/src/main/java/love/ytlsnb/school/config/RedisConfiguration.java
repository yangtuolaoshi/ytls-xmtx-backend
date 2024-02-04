package love.ytlsnb.school.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author ula
 */
@SpringBootConfiguration
public class RedisConfiguration {
    public static final String REDISSON_ADDRESS_PREFIX = "redis://";
    public static final String REDISSON_ADDRESS_SEPARATER = ":";
    @Autowired
    RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(REDISSON_ADDRESS_PREFIX + redisProperties.getHost() + REDISSON_ADDRESS_SEPARATER + redisProperties.getPort())
                .setPassword(redisProperties.getPassword())
                .setDatabase(redisProperties.getDatabase());
        return Redisson.create(config);
    }
}
