package love.ytlsnb.ad.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import love.ytlsnb.ad.service.AdvertisementSimilarityService;
import love.ytlsnb.ad.service.AdvertisementTagService;
import love.ytlsnb.ad.service.RecommendationScoreService;
import love.ytlsnb.ad.service.U2ABehaviorService;
import love.ytlsnb.common.constants.AdvertisementConstant;
import love.ytlsnb.model.ad.po.AdvertisementSimilarity;
import love.ytlsnb.model.ad.po.AdvertisementTag;
import love.ytlsnb.model.ad.po.RecommendationScore;
import love.ytlsnb.model.ad.po.U2ABehavior;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author ula
 * @date 2024/3/30 16:38
 */
@Component
public class AdMqListener {
    @Autowired
    private AdvertisementSimilarityService adSimilarityService;
    @Autowired
    private AdvertisementTagService adTagService;
    @Autowired
    private RecommendationScoreService recommendationScoreService;
    @Autowired
    private U2ABehaviorService u2aBehaviorService;

    @RabbitListener(queues = AdvertisementConstant.AD_SIMILARITY_DELETE_QUEUE_NAME)
    public void listen2DeleteAdSimilarity(Long adId) {
        adSimilarityService.remove(new LambdaQueryWrapper<AdvertisementSimilarity>()
                .eq(AdvertisementSimilarity::getAdvertisementId, adId));
    }

    @RabbitListener(queues = AdvertisementConstant.AD_TAG_DELETE_QUEUE_NAME)
    public void listen2DeleteAdTag(Long adId) {
        adTagService.remove(new LambdaQueryWrapper<AdvertisementTag>()
                .eq(AdvertisementTag::getAdvertisementId, adId));
    }

    @RabbitListener(queues = AdvertisementConstant.RECOMMENDATION_SCORE_DELETE_BYADID_QUEUE_NAME)
    public void listen2DeleteRecommendationScore(Long adId) {
        recommendationScoreService.remove(new LambdaQueryWrapper<RecommendationScore>()
                .eq(RecommendationScore::getAdvertisementId, adId));
    }

    @RabbitListener(queues = AdvertisementConstant.U2ABEHAVIOR_DELETE_BYADID_QUEUE_NAME)
    public void listen2DeleteU2ABehavior(Long adId) {
        u2aBehaviorService.remove(new LambdaQueryWrapper<U2ABehavior>()
                .eq(U2ABehavior::getAdvertisementId, adId));
    }
}
