package love.ytlsnb.ad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.ytlsnb.model.ad.po.RecommendationScore;

import java.util.List;

/**
 * @author ula
 * @date 2024/3/23 16:01
 */
public interface RecommendationScoreService extends IService<RecommendationScore> {
    List<RecommendationScore> listByUserId(Long userId);
}
