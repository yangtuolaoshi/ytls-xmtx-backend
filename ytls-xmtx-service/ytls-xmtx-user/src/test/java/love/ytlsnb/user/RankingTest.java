package love.ytlsnb.user;

import love.ytlsnb.model.user.po.User;
import love.ytlsnb.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;

import static love.ytlsnb.common.constants.RedisConstant.QUEST_FINISH_RANKING_PREFIX;

@SpringBootTest
public class RankingTest {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Test
    void addQuestFinishRanking() {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            Long userId = user.getId();
            Integer questFinishCount = user.getQuestFinishCount();
            zSetOperations.add(QUEST_FINISH_RANKING_PREFIX, userId.toString(), questFinishCount);
        }
    }
}
