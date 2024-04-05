package love.ytlsnb.common.constants;

import java.math.BigDecimal;

/**
 * @author ula
 * @date 2024/3/22 15:41
 */
public class AdvertisementConstant {
    public static final Byte DEFAULT_BEHAVIOR = 0;

    public static final Integer DEFAULT_BIGDECIMAL_SCALE = 10;

    //-------------------------Tag-------------------------
    public static final Integer DEFAULT_SIMILARITY_SCORE = 1;
    public static final Byte FIRST_LEVEL = 1;
    public static final Byte SECOND_LEVEL = 2;
    /**
     * 单个标签的价格（注：计算价格有单独的一套规则，不是简单的乘法）
     */
    public static final BigDecimal SINGLE_TAG_PRICE=new BigDecimal("1");
    /**
     * 标签折扣
     */
    public static final BigDecimal DISCOUNT=new BigDecimal("1.25");

    //-------------------------RabbitMQ-------------------------

    public static final String AD_DELETE_FANOUT_EXCHANGE_NAME="ad.delete.fanout";
    public static final String AD_TAG_DELETE_QUEUE_NAME="adTag.delete.queue";
    public static final String AD_SIMILARITY_DELETE_QUEUE_NAME="adSimilarity.delete.queue";
    public static final String RECOMMENDATION_SCORE_DELETE_BYADID_QUEUE_NAME="recommendationScore.delete.byAdId.queue";
    public static final String U2ABEHAVIOR_DELETE_BYADID_QUEUE_NAME="u2aBehavior.delete.byAdId.queue";

}
