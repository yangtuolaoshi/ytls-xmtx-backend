package love.ytlsnb.user.service;

import love.ytlsnb.model.quest.vo.RankingItemVO;

import java.util.List;

/**
 * 排行榜业务层接口
 *
 * @author 金泓宇
 * @date 2024/3/30
 */
public interface RankingService {
    /**
     * 拿到当前用户的排名
     * @return 当前用户的排名
     */
    RankingItemVO getMyRanking();

    /**
     * 获取排行榜
     * @param page 当前页
     * @param size 每页条数
     * @return 排行榜列表
     */
    List<RankingItemVO> getRanking(int page, int size);
}
