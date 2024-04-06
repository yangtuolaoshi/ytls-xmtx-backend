package love.ytlsnb.user;

import love.ytlsnb.model.common.LoginType;
import love.ytlsnb.model.common.Operator;
import love.ytlsnb.model.user.po.User;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;

/**
 * @author ula
 */
@SpringBootTest
public class UserTest {
//    @Autowired
//    UserController userController;
//
//    @Autowired
//    AliUtil aliUtil;
//    @Autowired
//    private RedissonClient redissonClient;
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//
//    @Test
//    public void test() throws Exception {
//        redisTemplate.opsForValue().set("test1:1","test1,1");
//        redisTemplate.opsForValue().set("test1:2","test1,2");
//        RBloomFilter<String> test1 = redissonClient.getBloomFilter("test1");
//        test1.tryInit(1000L,0.05);
//        boolean contains = test1.contains("1");
//        System.out.println(contains);
//        redisTemplate.opsForValue().set("test2:1","test2,1");
//        redisTemplate.opsForValue().set("test2:2","test2,2");
//        RBloomFilter<String> test2 = redissonClient.getBloomFilter("test2");
//        test2.tryInit(1000L,0.05);
//        test2.add("1");
//        test1.add("1");
//        System.out.println(test2.contains("1"));
//    }
//    @Test
//    public void testBD(){
//
//    }
}