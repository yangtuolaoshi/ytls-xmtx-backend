package love.ytlsnb.common.utils;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.constants.RedisConstant;
import love.ytlsnb.common.properties.CacheProperties;
import love.ytlsnb.model.common.RedisData;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.function.Function;

/**
 * @author ula
 * @date 2024/1/31 9:06
 */
@Component
@Slf4j
public class CacheClient {
    @Autowired
    @Qualifier("cacheRebuildExecutor")
    private ExecutorService cacheRebuildExecutor;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private CacheProperties cacheProperties;

    /**
     * 缓存数据的普通函数封装
     *
     * @param key   缓存的key
     * @param value 缓存的value
     * @param ttl   缓存对象的生存周期
     * @param unit  生存周期的时间单位
     */
    public void set(String key, Object value, Long ttl, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), ttl, unit);
    }

    /**
     * 热点数据的缓存建立，通过设置逻辑过期来解决缓存击穿问题
     *
     * @param key   缓存的key
     * @param value 缓存的value
     * @param ttl   缓存对象的生存周期
     * @param unit  生存周期的时间单位
     */
    public void setWithLogicalExpiration(String key, Object value, Long ttl, TimeUnit unit) {
        RedisData redisData = new RedisData();
        redisData.setExpirationTime(unit.toMillis(ttl) + System.currentTimeMillis());
        redisData.setData(value);

        redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    /**
     * 解决了缓存穿透的缓存查询，缓存未命中会直接查询数据库，根据数据库的查询结果设置空缓存或者缓存数据（使用默认缓存时长）
     *
     * @param keyPrefix  缓存数据在Redis中的存储key的前缀
     * @param id         缓存数据的主键
     * @param resultType 查询数据的具体类型
     * @param dbFallback 缓存未命中的降级策略
     * @param <R>        查询数据的类型
     * @param <ID>       查询数据的主键
     * @return 查询到的结果，可能为null
     */
    public <R, ID> R query(String keyPrefix, ID id, Class<R> resultType, Function<ID, R> dbFallback) {
        return query(keyPrefix, id, resultType, dbFallback, cacheProperties.getDefaultTtl(), TimeUnit.MILLISECONDS);
    }

    /**
     * 解决了缓存穿透的缓存查询，缓存未命中会直接查询数据库，根据数据库的查询结果设置空缓存或者缓存数据
     *
     * @param keyPrefix  缓存数据在Redis中的存储key的前缀
     * @param id         缓存数据的主键
     * @param resultType 查询数据的具体类型
     * @param dbFallback 缓存未命中的降级策略
     * @param ttl        经数据库查询后存入redis的生存周期
     * @param unit       生存周期的时间单位
     * @param <R>        查询数据的类型
     * @param <ID>       查询数据的主键
     * @return 查询到的结果，可能为null
     */
    public <R, ID> R query(String keyPrefix, ID id, Class<R> resultType, Function<ID, R> dbFallback, Long ttl, TimeUnit unit) {
        String key = keyPrefix + id;
        String value = redisTemplate.opsForValue().get(key);

        if (StrUtil.isNotBlank(value)) {
            // 查出数据不为空，直接返回
            return JSONUtil.toBean(value, resultType);
        }
        // 查出数据为空字符串，表示数据库中没有数据
        if (value != null) {
            return null;
        }

        // 查出数据为null，该项数据没有经数据库查询过
        R r = dbFallback.apply(id);
        // 查询数据库后仍未查到数据
        if (r == null) {
            // 设置空缓存
            redisTemplate.opsForValue().set(key, "", cacheProperties.getNullTtl(), unit);
            return null;
        }

        // 查询到了数据，建立缓存
        redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(r), ttl, unit);
        return r;
    }

    /**
     * 热点数据的查询，会对数据进行封装，设置逻辑过期时间，逻辑过期后进行异步的缓存建立，用以解决缓存击穿问题
     *
     * @param keyPrefix  缓存数据在Redis中的存储key的前缀
     * @param id         缓存数据的主键
     * @param resultType 查询数据的具体类型
     * @param dbFallback 缓存未命中的降级策略
     * @param <R>        查询数据的类型
     * @param <ID>       查询数据的主键
     * @return 查询到的结果，可能为null
     */
    public <R, ID> R queryWithLogicalExpiration(String keyPrefix, ID id, Class<R> resultType, Function<ID, R> dbFallback) {
        return queryWithLogicalExpiration(keyPrefix, id, resultType, dbFallback, cacheProperties.getDefaultTtl(), TimeUnit.MILLISECONDS);
    }

    /**
     * 热点数据的查询，会对数据进行封装，设置逻辑过期时间，逻辑过期后进行异步的缓存建立，用以解决缓存击穿问题
     *
     * @param keyPrefix  缓存数据在Redis中的存储key的前缀
     * @param id         缓存数据的主键
     * @param resultType 查询数据的具体类型
     * @param dbFallback 缓存未命中的降级策略
     * @param ttl        经数据库查询后存入redis的生存周期
     * @param unit       生存周期的时间单位
     * @param <R>        查询数据的类型
     * @param <ID>       查询数据的主键
     * @return 查询到的结果，可能为null
     */
    public <R, ID> R queryWithLogicalExpiration(String keyPrefix, ID id, Class<R> resultType, Function<ID, R> dbFallback, Long ttl, TimeUnit unit) {
        String key = keyPrefix + id;
        String value = redisTemplate.opsForValue().get(key);

        if (StrUtil.isNotBlank(value)) {
            // 查出数据不为空
            RedisData redisData = JSONUtil.toBean(value, RedisData.class);
            Long expirationTime = redisData.getExpirationTime();

            if (expirationTime > System.currentTimeMillis()) {
                // 数据未过期
                return JSONUtil.toBean((JSONObject) redisData.getData(), resultType);
            }

            // 数据已过期，异步进行缓存重建
            RLock lock = redissonClient.getLock(RedisConstant.LOCK_PREFIX + key);
            boolean success = lock.tryLock();
            if (!success) {
                // 返回过期数据
                return JSONUtil.toBean((JSONObject) redisData.getData(), resultType);
            }
            // double check
            value = redisTemplate.opsForValue().get(key);
            redisData = JSONUtil.toBean(value, RedisData.class);
            expirationTime = redisData.getExpirationTime();
            if (expirationTime > System.currentTimeMillis()) {
                // 新建立的缓存，直接返回
                return JSONUtil.toBean((JSONObject) redisData.getData(), resultType);
            }
            // 缓存重建任务提交
            cacheRebuildExecutor.submit(() -> {
                try {
                    R r = dbFallback.apply(id);
                    log.info("正在异步建立缓存:{}", r);
                    if (r != null) {
                        setWithLogicalExpiration(key, r, ttl, unit);
                    } else {
                        // 数据库中数据被删除，设置空缓存
                        redisTemplate.opsForValue().set(key, "", ttl, unit);
                    }
                } finally {
                    try {
                        lock.unlock();
                    } catch (Exception e) {
                        log.error("释放锁失败:{},锁已经释放", lock);
                    }
                }
            });
            // 返回过期数据
            return JSONUtil.toBean((JSONObject) redisData.getData(), resultType);
        } else {
            // 查出数据为空，一般需要设置逻辑过期的数据会在项目启动时加载，所以项目启动后在数据存在的情况下只会有一次查出数据为空
            if (value != null) {
                // 查出数据为空字符串，表示目前数据库中没有数据
                return null;
            }
            // 查出数据为null，该项数据没有经数据库查询过，这里同样进行异步缓存建立
            RLock lock = redissonClient.getLock(RedisConstant.LOCK_PREFIX + key);
            // 多线程下仅允许一个线程提交缓存重建任务
            boolean success = lock.tryLock();
            if (!success) {
                return null;
            }
            // double check
            value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                // 有线程建立过缓存了
                RedisData redisData = JSONUtil.toBean(value, RedisData.class);
                Long expirationTime = redisData.getExpirationTime();
                if (expirationTime > System.currentTimeMillis()) {
                    // 新建立的缓存，直接返回
                    return JSONUtil.toBean((JSONObject) redisData.getData(), resultType);
                }
            }
            // 缓存重建任务提交
            cacheRebuildExecutor.submit(() -> {
                try {
                    R r = dbFallback.apply(id);
                    log.info("正在异步建立缓存:{}", r);
                    if (r != null) {
                        setWithLogicalExpiration(key, r, ttl, unit);
                    } else {
                        // 数据库中也没有数据，设置空缓存
                        redisTemplate.opsForValue().set(key, "", ttl, unit);
                    }
                } finally {
                    try {
                        lock.unlock();
                    } catch (Exception e) {
                        log.error("释放锁失败:{},锁已经释放", lock);
                    }
                }
            });
            return null;
        }
    }
}
