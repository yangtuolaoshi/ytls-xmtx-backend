package love.ytlsnb.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import love.ytlsnb.common.utils.UserHolder;
import love.ytlsnb.model.quest.vo.RankingItemVO;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.user.mapper.UserMapper;
import love.ytlsnb.user.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static love.ytlsnb.common.constants.RedisConstant.POINT_RANKING_PREFIX;

/**
 * 排行榜业务层实现类
 *
 * @author 金泓宇
 * @date 2024/3/30
 */
@Service
public class RankingServiceImpl implements RankingService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public RankingItemVO getMyRanking() {
        Long userId = UserHolder.getUser().getId();
        User user = userMapper.selectById(userId);
        RankingItemVO rankingItemVO = new RankingItemVO();
        // 查询排行
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        Long rank = zSetOperations.reverseRank(POINT_RANKING_PREFIX, userId + "");
        BeanUtil.copyProperties(user, rankingItemVO);
        rankingItemVO.setRank(rank);
        rankingItemVO.setUserId(userId);
        return rankingItemVO;
    }

    @Override
    public List<RankingItemVO> getRanking(int page, int size) {
        List<RankingItemVO> rankingItemVOs = new ArrayList<>();
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        Set<String> range = zSetOperations.reverseRange(POINT_RANKING_PREFIX, (long) (page - 1) * size, size);
        List<User> users = userMapper.selectBatchIds(range);
        for (User user : users) {
            RankingItemVO rankingItemVO = new RankingItemVO();
            BeanUtil.copyProperties(user, rankingItemVO);
            rankingItemVO.setUserId(user.getId());
            rankingItemVOs.add(rankingItemVO);
        }
        // 排序
        rankingItemVOs.sort((r1, r2) -> r2.getPoint() - r1.getPoint());
        return rankingItemVOs;
    }
}
