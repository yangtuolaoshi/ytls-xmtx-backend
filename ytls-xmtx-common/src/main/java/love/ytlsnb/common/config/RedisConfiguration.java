package love.ytlsnb.common.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ula
 * @date 2024/2/28 20:29
 */
@Configuration
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

    @Bean
    public ExecutorService cacheRebuildExecutor() {
        return new ThreadPoolExecutor(
                3, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), new ThreadFactoryBuilder()
                .setNamePrefix("cache-pool-").build(), new ThreadPoolExecutor.AbortPolicy());
    }
}