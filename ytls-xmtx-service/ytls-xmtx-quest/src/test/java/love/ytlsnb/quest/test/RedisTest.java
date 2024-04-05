package love.ytlsnb.quest.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import love.ytlsnb.common.utils.UserHolder;
import love.ytlsnb.model.quest.po.QuestLocation;
import love.ytlsnb.model.quest.po.QuestSchedule;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.quest.mapper.QuestLocationMapper;
import love.ytlsnb.quest.mapper.QuestScheduleMapper;
import love.ytlsnb.quest.service.QuestScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;

import static love.ytlsnb.common.constants.RedisConstant.QUEST_SCHEDULE_GEO_PREFIX;

@SpringBootTest
public class RedisTest {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void testRedisClient() {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("name", "zhangsan");
        System.out.println(valueOperations.get("name"));
    }

    @Autowired
    private QuestLocationMapper questLocationMapper;

    @Autowired
    private QuestScheduleMapper questScheduleMapper;

    /**
     * 添加数据到Redis
     */
    @Test
    void addGeo() {
        GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
        List<QuestLocation> questLocations = questLocationMapper.selectList(null);
        for (QuestLocation questLocation : questLocations) {
            Double longitude = questLocation.getLongitude();
            Double latitude = questLocation.getLatitude();
            LambdaQueryWrapper<QuestSchedule> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(QuestSchedule::getLocationId, questLocation.getId());
            QuestSchedule questSchedule = questScheduleMapper.selectOne(queryWrapper);
            geoOperations.add(
                    QUEST_SCHEDULE_GEO_PREFIX + 1,
                    new Point(longitude, latitude),
                    questSchedule.getId().toString()
            );
        }
    }

    @Autowired
    private QuestScheduleService questScheduleService;

    @Test
    void testGetNearest() {
        User user = new User();
        user.setId(1758018823723806722L);
        user.setSchoolId(1L);
        UserHolder.saveUser(user);
        System.out.println(questScheduleService.getNearest(119.51602, 32.20073));
    }
}
