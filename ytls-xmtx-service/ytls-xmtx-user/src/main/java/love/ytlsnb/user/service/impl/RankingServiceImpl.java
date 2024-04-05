package love.ytlsnb.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import love.ytlsnb.common.exception.BusinessException;
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
import static love.ytlsnb.common.constants.RedisConstant.QUEST_FINISH_RANKING_PREFIX;
import static love.ytlsnb.common.constants.ResultCodes.NOT_ACCEPTABLE;

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
    public RankingItemVO getMyPointRanking(Integer type) {
        Long userId = UserHolder.getUser().getId();
        User user = userMapper.selectById(userId);
        RankingItemVO rankingItemVO = new RankingItemVO();
        // 查询排行
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        String prefix;
        if (type == 1) {
            prefix = POINT_RANKING_PREFIX;
        } else if (type == 2) {
            prefix = QUEST_FINISH_RANKING_PREFIX;
        } else {
            prefix = "";
        }
        Long rank = zSetOperations.reverseRank(prefix, userId + "");
        BeanUtil.copyProperties(user, rankingItemVO);
        rankingItemVO.setRank(rank);
        rankingItemVO.setUserId(userId);
        return rankingItemVO;
    }

    @Override
    public List<RankingItemVO> getRanking(Integer type, int page, int size) {
        if (type == null) {
            throw new BusinessException(NOT_ACCEPTABLE, "非法的请求参数");
        }
        List<RankingItemVO> rankingItemVOs = new ArrayList<>();
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        String prefix;
        if (type == 1) {
            prefix = POINT_RANKING_PREFIX;
        } else if (type == 2) {
            prefix = QUEST_FINISH_RANKING_PREFIX;
        } else {
            prefix = "";
        }
        Set<String> range = zSetOperations.reverseRange(prefix, (long) (page - 1) * size, size);
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
