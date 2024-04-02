package love.ytlsnb.ad.config;

import love.ytlsnb.common.constants.AdvertisementConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ula
 * @date 2024/3/30 14:38
 */
@Configuration
public class AdMqConfiguration {
    @Bean
    public FanoutExchange adDeleteFanoutExchange() {
        return new FanoutExchange(AdvertisementConstant.AD_DELETE_FANOUT_EXCHANGE_NAME);
    }

    @Bean
    public Queue adSimilarityDeleteQueue() {
        return new Queue(AdvertisementConstant.AD_SIMILARITY_DELETE_QUEUE_NAME);
    }

    @Bean
    public Queue adTagDeleteQueue() {
        return new Queue(AdvertisementConstant.AD_TAG_DELETE_QUEUE_NAME);
    }

    @Bean
    public Queue recommendationScoreDeleteQueue() {
        return new Queue(AdvertisementConstant.RECOMMENDATION_SCORE_DELETE_BYADID_QUEUE_NAME);
    }

    @Bean
    public Queue u2aBehaviorDeleteQueue() {
        return new Queue(AdvertisementConstant.U2ABEHAVIOR_DELETE_BYADID_QUEUE_NAME);
    }

    @Bean
    public Binding bindingAdSimilarityDeleteQueue2AdDeleteFanout() {
        return BindingBuilder.bind(adSimilarityDeleteQueue())
                .to(adDeleteFanoutExchange());
    }

    @Bean
    public Binding bindingAdTagDeleteQueue2AdDeleteFanout() {
        return BindingBuilder.bind(adTagDeleteQueue())
                .to(adDeleteFanoutExchange());
    }

    @Bean
    public Binding bindingRecommendationScoreDeleteQueue2AdDeleteFanout() {
        return BindingBuilder.bind(recommendationScoreDeleteQueue())
                .to(adDeleteFanoutExchange());
    }

    @Bean
    public Binding bindingU2ABehaviorDeleteQueue2AdDeleteFanout() {
        return BindingBuilder.bind(u2aBehaviorDeleteQueue())
                .to(adDeleteFanoutExchange());
    }
}
