package love.ytlsnb.school.utils;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import love.ytls.api.school.SchoolClient;
import love.ytls.api.user.UserClient;
import love.ytlsnb.common.constants.RedisConstant;
import love.ytlsnb.common.constants.UserConstant;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.school.po.School;
import love.ytlsnb.model.user.dto.UserQueryDTO;
import love.ytlsnb.model.user.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ula
 * @date 2024/2/3 15:40
 */
@Component
@Slf4j
public class SchoolUtil {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    UserClient userClient;
    @Autowired
    SchoolClient schoolClient;

    /**
     * 更新所有学校的排行总榜
     */
    public void updateRankingListTotal() {
        // 获取学校列表
        Result<List<School>> schoolListResult = schoolClient.list();
        List<School> schoolList = schoolListResult.getData();

        // 加载每个学校的排行总榜
        for (School school : schoolList) {
            Long schoolId = school.getId();

            // 根据学校信息查询相关用户信息
            UserQueryDTO userQueryDTO = new UserQueryDTO();
            userQueryDTO.setIdentified(UserConstant.IDENTIFIED);
            userQueryDTO.setSchoolId(schoolId);

//            Result<List<User>> userListResult = userClient.list(userQueryDTO);
//            List<User> userList = userListResult.getData();
//
//            if (userList != null && userList.size() != 0) {
//                // 添加用户至redis
//                for (User user : userList) {
//                    redisTemplate.opsForZSet().add(RedisConstant.SCHOOL_RANKING_LIST_TOTAL_PREFIX + schoolId,
//                            user.getId().toString(),
//                            user.getPoint());
//                }
//            }
        }
    }

    // TODO 需要任务服务，任务服务尚未开发完成
    public void updateRankingListWeekly() {
        // 获取学校列表
        Result<List<School>> schoolListResult = schoolClient.list();
        List<School> schoolList = schoolListResult.getData();

        // 加载每个学校的排行月榜
        for (School school : schoolList) {
            Long schoolId = school.getId();

            // 根据学校信息查询相关用户信息
            UserQueryDTO userQueryDTO = new UserQueryDTO();
            userQueryDTO.setIdentified(UserConstant.IDENTIFIED);
            userQueryDTO.setSchoolId(schoolId);

//            Result<List<User>> userListResult = userClient.list(userQueryDTO);
//            List<User> userList = userListResult.getData();
//
//            if (userList != null && userList.size() != 0) {
//                // 添加用户信息至redis
//                for (User user : userList) {
//
//                    redisTemplate.opsForZSet().add(RedisConstant.SCHOOL_RANKING_LIST_TOTAL_PREFIX + schoolId,
//                            user.getId().toString(),
//                            user.getPoint());
//                }
//            }
        }
    }

    // TODO 需要任务服务，任务服务尚未开发完成
    public void updateRankingListDayly() {
        // 获取学校列表
        Result<List<School>> schoolListResult = schoolClient.list();
        List<School> schoolList = schoolListResult.getData();

        // 加载每个学校的排行总榜
        for (School school : schoolList) {
            Long schoolId = school.getId();

            // 根据学校信息查询相关用户信息
            UserQueryDTO userQueryDTO = new UserQueryDTO();
            userQueryDTO.setIdentified(UserConstant.IDENTIFIED);
            userQueryDTO.setSchoolId(schoolId);

//            Result<List<User>> userListResult = userClient.list(userQueryDTO);
//            List<User> userList = userListResult.getData();
//
//            if (userList != null && userList.size() != 0) {
//                // 添加用户至redis
//                for (User user : userList) {
//                    redisTemplate.opsForZSet().add(RedisConstant.SCHOOL_RANKING_LIST_TOTAL_PREFIX + schoolId,
//                            user.getId().toString(),
//                            user.getPoint());
//                }
//            }
        }
    }
}
