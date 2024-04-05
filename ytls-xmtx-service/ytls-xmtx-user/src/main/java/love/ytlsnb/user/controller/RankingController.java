package love.ytlsnb.user.controller;

import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.vo.RankingItemVO;
import love.ytlsnb.user.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 排行榜表现层
 *
 * @author 金泓宇
 * @date 2024/3/29
 */
@RestController
@RequestMapping("/ranking")
public class RankingController {
    @Autowired
    private RankingService rankingService;

    /**
     * 获取当前用户的排行
     * @return 当前用户的排行
     */
    @GetMapping("/me")
    public Result<RankingItemVO> getMyRanking() {
        return Result.ok(rankingService.getMyRanking());
    }

    /**
     * 获取排行榜
     * @param page 当前页
     * @param size 每页条数
     * @return 排行榜列表
     */
    @GetMapping("/top")
    public Result<List<RankingItemVO>> getRanking(int page, int size) {
        return Result.ok(rankingService.getRanking(page, size));
    }
}
