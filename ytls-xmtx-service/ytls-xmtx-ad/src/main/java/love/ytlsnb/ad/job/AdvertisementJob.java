package love.ytlsnb.ad.job;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import love.ytls.api.user.UserClient;
import love.ytlsnb.ad.service.*;
import love.ytlsnb.common.constants.AdvertisementConstant;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.model.ad.dto.UserBehaviorScoreDTO;
import love.ytlsnb.model.ad.po.*;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.user.dto.UserQueryDTO;
import love.ytlsnb.model.user.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * @author ula
 * @date 2024/3/22 13:48
 */
@Slf4j
@Component
public class AdvertisementJob {
    @Autowired
    private UserClient userClient;
    @Autowired
    private AdvertisementService adService;
    @Autowired
    private AdvertisementSimilarityService adSimilarityService;
    @Autowired
    private RecommendationScoreService recommendationScoreService;
    @Autowired
    private TagService tagService;
    @Autowired
    private U2ABehaviorService u2aBehaviorService;
    @Autowired
    @Qualifier("advertisementJobExecutor")
    private ExecutorService advertisementJobExecutor;

    /**
     * 计算用户-广告推荐分值，内部采用CB-CF加权统计
     */
    @XxlJob("calculateRecommendationScore")
    @Transactional
    public void calculateRecommendationScore() throws InterruptedException {
        // 根据分片参数获取所有用户数据集
        log.info("执行calculateRecommendationScore");
//        int shardIndex = XxlJobHelper.getShardIndex();
//        int shardTotal = XxlJobHelper.getShardTotal();
        int shardIndex = 1;
        int shardTotal = 2;
        Result<List<User>> userListResult = userClient.listBySharding(shardTotal, shardIndex);
        if (userListResult.getCode() != ResultCodes.OK) {
            throw new BusinessException(userListResult.getCode(), userListResult.getMsg());
        }
        List<User> userList = userListResult.getData();
        List<Advertisement> adList = adService.list();

        // <用户ID，<广告ID，推荐分值>>
        Map<Long, Map<Long, BigDecimal>> cfScores = getCFScore(userList, adList);
        Map<Long, Map<Long, BigDecimal>> cbScores = getCBScore(userList, adList);

        List<RecommendationScore> recommendationScoreList = new ArrayList<>();
        for (User user : userList) {
            for (Advertisement ad : adList) {
                BigDecimal sum = BigDecimal.ZERO;
                if (cfScores.containsKey(user.getId())) {
                    Map<Long, BigDecimal> cfScore = cfScores.get(user.getId());
                    if (cfScore.containsKey(ad.getId())) {
                        sum = sum.add(cfScore.get(ad.getId()));
                    }
                }
                if (cbScores.containsKey(user.getId())) {
                    Map<Long, BigDecimal> cbScore = cbScores.get(user.getId());
                    if (cbScore.containsKey(ad.getId())) {
                        sum = sum.add(cbScore.get(ad.getId()));
                    }
                }
                if (sum.compareTo(BigDecimal.ONE) <= 0) {
                    sum = BigDecimal.ONE;
                }
                RecommendationScore score = RecommendationScore.builder()
                        .userId(user.getId())
                        .advertisementId(ad.getId())
                        .score(sum)
                        .build();
                recommendationScoreList.add(score);
            }
        }
        recommendationScoreService.saveOrUpdateBatch(recommendationScoreList);
    }

    /**
     * 采用皮尔森相似度算法进行计算
     */
    @XxlJob("calculateAdvertisementSimilarity")
    public void calculateAdvertisementSimilarity() throws InterruptedException {
        // 获取当前分片参数下所有广告集
//        int shardIndex = XxlJobHelper.getShardIndex();
//        int shardTotal = XxlJobHelper.getShardTotal();
        int shardIndex = 1;
        int shardTotal = 2;

        List<Advertisement> adList = adService.listBySharding(shardTotal, shardIndex);
        // 提前获取所有广告集
        List<Advertisement> allAdList = adService.list();
        // 提前查询用户对于广告的操作集，封装到map中   <adID,<userId,dto>>
        Map<Long, Map<Long, BigDecimal>> u2aBehaviorScoreMap = new HashMap<>();
        // 查询所有用户数 TODO(后续可以给用户添加属性，标记为当前年级的学生，将数据集改为当前年级的学生)
        Result<List<User>> userListResult = userClient.list();
        if (userListResult.getCode() != ResultCodes.OK) {
            throw new BusinessException(userListResult.getCode(), userListResult.getMsg());
        }
        List<User> userList = userListResult.getData();
        Map<Long, BigDecimal> u2aAvgBehaviorScoreMap = new HashMap<>();
        CountDownLatch prepareCountDownLatch = new CountDownLatch(allAdList.size());
        for (Advertisement ad : allAdList) {
            advertisementJobExecutor.submit(() -> {
                // 获取当前广告的所有用户相关操作
                List<UserBehaviorScoreDTO> userBehaviorScoreList = u2aBehaviorService.listScoreByAdId(ad.getId());
                Map<Long, BigDecimal> userBehaviorScoreMap = userBehaviorScoreList.stream().collect(Collectors
                        .toMap(UserBehaviorScoreDTO::getUserId, UserBehaviorScoreDTO::getScore));
                u2aBehaviorScoreMap.put(ad.getId(), userBehaviorScoreMap);
                if (userList.size() != 0) {
                    BigDecimal u2aBehaviorScoreSum = userBehaviorScoreList.stream()
                            .map(UserBehaviorScoreDTO::getScore)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    u2aAvgBehaviorScoreMap.put(ad.getId(),
                            u2aBehaviorScoreSum.divide(new BigDecimal(userList.size()),
                                    AdvertisementConstant.DEFAULT_BIGDECIMAL_SCALE,
                                    RoundingMode.HALF_UP));
                }
                // 执行完成后计数器减一
                prepareCountDownLatch.countDown();
                log.info("广告准备工作完成{}", ad.getId());
            });
        }
        prepareCountDownLatch.await();
        for (Advertisement ad : adList) {
            advertisementJobExecutor.submit(() -> {
                // 因为广告相似度是一个对称矩阵，这里查看当前广告的矩阵对称位置是否已经更新，减少计算量
                List<AdvertisementSimilarity> calculatedAdSimilarityList = adSimilarityService.listBySimilarAdId(ad.getId());
                // 过滤出已经被计算过的广告相似度集合，如果更新时间不是在凌晨00:00，则需修改代码逻辑或者直接注释当前代码
                Map<Long, AdvertisementSimilarity> calculatedAdSimilarityMap = calculatedAdSimilarityList.stream()
                        .filter(ads -> ads.getUpdateTime().isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)))
                        .collect(Collectors.toMap(AdvertisementSimilarity::getAdvertisementId, o -> o));
                // 当前广告和所有广告的相似度集合
                List<AdvertisementSimilarity> adSimilarityList = new ArrayList<>();
                // 遍历所有广告集，计算当前广告与所有广告的相似度
                for (Advertisement similarAd : allAdList) {
                    // 查看当前广告-广告相似度是否已经计算过
                    if (calculatedAdSimilarityMap.containsKey(similarAd.getId())) {
                        // 已经计算过
                        AdvertisementSimilarity calculatedAdSimilarity = calculatedAdSimilarityMap.get(similarAd.getId());
                        adSimilarityList.add(AdvertisementSimilarity.builder()
                                .advertisementId(ad.getId())
                                .similarAdvertisementId(similarAd.getId())
                                .similarity(calculatedAdSimilarity.getSimilarity())
                                .build());
                    } else {
                        // 还未计算过，进行计算
                        if (ad.getId().equals(similarAd.getId())) {
                            // 自身和自身的相似度为一
                            adSimilarityList.add(AdvertisementSimilarity.builder()
                                    .advertisementId(ad.getId())
                                    .similarAdvertisementId(similarAd.getId())
                                    .similarity(BigDecimal.ONE)
                                    .build());
                            continue;
                        }
                        // 取出当前待计算广告和对象广告的用户相关操作集合  <userId,score>
                        Map<Long, BigDecimal> u2aMap = u2aBehaviorScoreMap.get(ad.getId());
                        Map<Long, BigDecimal> u2saMap = u2aBehaviorScoreMap.get(similarAd.getId());

                        // 皮尔森相似度计算公式中的分子分母
                        BigDecimal numerator = BigDecimal.ZERO;
                        BigDecimal aDenominator = BigDecimal.ZERO;
                        BigDecimal saDenominator = BigDecimal.ZERO;
                        for (User user : userList) {
                            Long userId = user.getId();
                            BigDecimal u2aScore = u2aMap.get(userId);
                            if (u2aScore == null) {
                                u2aScore = BigDecimal.ZERO;
                            }
                            BigDecimal u2saScore = u2aMap.get(userId);
                            if (u2saScore == null) {
                                u2saScore = BigDecimal.ZERO;
                            }
                            // 更新分子值
                            numerator = numerator.add(u2aScore.subtract(u2aAvgBehaviorScoreMap.get(ad.getId()))
                                    .multiply(u2saScore.subtract(u2aAvgBehaviorScoreMap.get(similarAd.getId()))));

                            // 更新分母值
                            aDenominator = aDenominator
                                    .add(u2aScore.subtract(u2aAvgBehaviorScoreMap.get(ad.getId()))
                                            .pow(2));
                            saDenominator = saDenominator
                                    .add(u2saScore.subtract(u2aAvgBehaviorScoreMap.get(similarAd.getId()))
                                            .pow(2));
                        }
                        BigDecimal res;
                        if (numerator.compareTo(BigDecimal.ZERO) <= 0) {
                            // 分子为零，直接赋值相似度为零
                            res = BigDecimal.ZERO;
                        } else {
                            res = numerator.divide(aDenominator.sqrt(MathContext.DECIMAL64),
                                            AdvertisementConstant.DEFAULT_BIGDECIMAL_SCALE,
                                            RoundingMode.HALF_UP)
                                    .divide(saDenominator.sqrt(MathContext.DECIMAL64),
                                            AdvertisementConstant.DEFAULT_BIGDECIMAL_SCALE,
                                            RoundingMode.HALF_UP);
                        }
                        adSimilarityList.add(AdvertisementSimilarity.builder()
                                .advertisementId(ad.getId())
                                .similarAdvertisementId(similarAd.getId())
                                .similarity(res)
                                .build());
                    }
                }
                // 更新数据，落库
                adSimilarityService.saveOrUpdateBatch(adSimilarityList);
                log.info("广告的广告相似度计算完成{}", ad.getId());
            });
        }
    }

    /**
     * 根据传入用户集计算其针对广告（用户可能喜欢的广告集，非全集）的CB推荐得分
     *
     * @param userList 待计算的用户集
     * @return 根据CB计算出的用户广告推荐分值（此处包含所有广告集）
     */
    private Map<Long, Map<Long, BigDecimal>> getCBScore(List<User> userList, List<Advertisement> adList) throws InterruptedException {
        // 获取所有广告的标签
        Map<Long, List<Tag>> adTagMap = new HashMap<>();
        for (Advertisement ad : adList) {
            List<Tag> tagList = tagService.listAllByAdvertisementId(ad.getId());
            adTagMap.put(ad.getId(), tagList);
        }

        // 最终结果集：每个用户的针对于每条广告的操作得分
        Map<Long, Map<Long, BigDecimal>> finalResultMap = new HashMap<>();
        CountDownLatch countDownLatch = new CountDownLatch(userList.size());
        // 计算用户对于所有广告的CB推荐分值
        for (User user : userList) {
            advertisementJobExecutor.submit(() -> {
                // 当前用户对于所有广告的CB推荐分值
                Map<Long, BigDecimal> userRecommendationScoreMap = new HashMap<>();

                // 获取当前用户的标签集合
                List<Tag> userTagList = tagService.listAllByUserId(user.getId());
                Set<Tag> userTagSet = new HashSet<>(userTagList);
                Set<Long> userParentTagSet = userTagList.stream().map(Tag::getParentId).collect(Collectors.toSet());

                for (Advertisement ad : adList) {
                    // 计算用户对于每个广告的CB推荐分
                    List<Tag> adTagList = adTagMap.get(ad.getId());
                    Set<Tag> adTagSet = new HashSet<>(adTagList);
                    Set<Long> adParentTagSet = adTagList.stream().map(Tag::getParentId).collect(Collectors.toSet());

                    Set<Long> similarTagSet = new HashSet<>();
                    // 获取用户与广告的相似标签
                    for (Long parentId : userParentTagSet) {
                        if (adParentTagSet.contains(parentId)) {
                            similarTagSet.add(parentId);
                        }
                    }
                    // 计算用户和广告在每个相似标签下的推荐分值，并求和
                    BigDecimal userAdScore = BigDecimal.ZERO;
                    for (Long pid : similarTagSet) {
                        Integer userScore = userTagSet.stream()
                                .filter(t -> t.getParentId().equals(pid))
                                .mapToInt(tag -> {
                                    if (adTagSet.contains(tag)) {
                                        return tag.getLevel();
                                    } else {
                                        return AdvertisementConstant.DEFAULT_SIMILARITY_SCORE;
                                    }
                                }).sum();
                        Integer adScore = adTagSet.stream()
                                .filter(t -> t.getParentId().equals(pid))
                                .mapToInt(tag -> {
                                    if (userTagSet.contains(tag)) {
                                        return tag.getLevel();
                                    } else {
                                        return AdvertisementConstant.DEFAULT_SIMILARITY_SCORE;
                                    }
                                }).sum();
                        userAdScore = userAdScore.add(new BigDecimal(2 * userScore * adScore)
                                .divide(new BigDecimal(userScore + adScore), AdvertisementConstant.DEFAULT_BIGDECIMAL_SCALE, RoundingMode.HALF_UP));
                    }
                    userRecommendationScoreMap.put(ad.getId(), userAdScore);
                }
                finalResultMap.put(user.getId(), userRecommendationScoreMap);
                log.info("用户的CB推荐分值计算完成{}", user.getNickname());
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        return finalResultMap;
    }

    /**
     * 根据传入用户集计算其针对广告（用户可能喜欢的广告集，非全集）的CF推荐得分
     *
     * @param userList 待计算的用户集
     * @return 根据CF计算出的用户广告推荐分值（此处仅包含用户可能喜欢的广告集）
     */
    private Map<Long, Map<Long, BigDecimal>> getCFScore(List<User> userList, List<Advertisement> adList) throws InterruptedException {
        Map<Long, BigDecimal> adAvgScoreMap = new HashMap<>();
        // 求出所有广告的用户平均操作分值
        for (Advertisement ad : adList) {
            BigDecimal sumScore = u2aBehaviorService.getAdSumScoreByAdId(ad.getId());
            BigDecimal avgScore = sumScore.divide(new BigDecimal(userList.size()),
                    AdvertisementConstant.DEFAULT_BIGDECIMAL_SCALE,
                    RoundingMode.HALF_UP);
            adAvgScoreMap.put(ad.getId(), avgScore);
        }

        // 最终结果集：每个用户的针对于每条广告的操作得分
        Map<Long, Map<Long, BigDecimal>> finalResultMap = new HashMap<>();
        CountDownLatch countDownLatch = new CountDownLatch(userList.size());
        // 待计算的用户集合
        for (User user : userList) {
            advertisementJobExecutor.submit(() -> {
                log.info("开始计算用户的CF得分集:{}", user.getNickname());
                // 待计算的广告集
                Set<Advertisement> adSet = new HashSet<>();
                // 计算结果集（当前用户对于每个广告的推荐分值）
                Map<Long, BigDecimal> recommendationScoreMap = new HashMap<>();

                // 获取每个用户的喜欢广告（目前只要包含操作，就认为用户喜欢，若没有用户喜欢的广告，就使用全广告集）
                List<Long> idList = u2aBehaviorService.listLikedAdIdsByUserId(user.getId());
                List<Advertisement> likedAdList;
                if (CollectionUtil.isEmpty(idList)) {
                    likedAdList = adList;
                } else {
                    likedAdList = adService.listByIds(idList);
                }
                // 获取每个用户喜欢广告的相似广告，并加入待计算的广告集合中
                for (Advertisement likedAd : likedAdList) {
                    List<Long> subIdList = adSimilarityService.listSimilarAdIdByAdId(likedAd.getId());
                    List<Advertisement> similarAdList;
                    if (!CollectionUtil.isEmpty(idList)) {
                        similarAdList = adService.listByIds(subIdList);
                        adSet.addAll(similarAdList);
                    }
                }

                // 获取用户对于所有广告的操作分值
                List<UserBehaviorScoreDTO> userBehaviorScoreList = u2aBehaviorService.listScoreByUserId(user.getId());
                Map<Long, BigDecimal> userBehaviorScoreMap = userBehaviorScoreList.stream().collect(Collectors.
                        toMap(UserBehaviorScoreDTO::getAdvertisementId,
                                UserBehaviorScoreDTO::getScore));
                // 计算每个广告的用户-广告推荐分
                for (Advertisement ad : adSet) {
                    // 获取当前广告对于所有广告的相似度
                    List<AdvertisementSimilarity> adSimilarityList = adSimilarityService
                            .list(new LambdaQueryWrapper<AdvertisementSimilarity>()
                                    .eq(AdvertisementSimilarity::getAdvertisementId, ad.getId()));
                    // 当前广告对于所有广告的相似度map
                    Map<Long, BigDecimal> similarityMap = adSimilarityList.stream().collect(Collectors
                            .toMap(AdvertisementSimilarity::getSimilarAdvertisementId,
                                    AdvertisementSimilarity::getSimilarity));
                    BigDecimal res = adAvgScoreMap.get(ad.getId());
                    BigDecimal weightSumOfScore = BigDecimal.ZERO;
                    // 遍历加权
                    for (Advertisement weightedAd : adList) {
                        // 待加权的广告ID
                        Long weightedAdId = weightedAd.getId();
                        // 当前用户对于该广告的操作得分
                        BigDecimal originalScore = userBehaviorScoreMap.get(weightedAdId);
                        BigDecimal score = originalScore == null ? BigDecimal.ZERO : originalScore;
                        // 更新加权总分
                        BigDecimal similarity = similarityMap.get(weightedAdId);
                        weightSumOfScore = weightSumOfScore
                                .add(score.subtract(adAvgScoreMap.get(weightedAdId))
                                        .multiply(similarity == null ? BigDecimal.ZERO : similarity));
                    }
                    // 所有广告的相似度总和
                    BigDecimal sumOfSimilarity = similarityMap.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                    // 更新结果值
                    res = res.add(weightSumOfScore.divide(sumOfSimilarity,
                            AdvertisementConstant.DEFAULT_BIGDECIMAL_SCALE,
                            RoundingMode.HALF_UP));
                    recommendationScoreMap.put(ad.getId(), res);
                }
                finalResultMap.put(user.getId(), recommendationScoreMap);
                log.info("用户的CF推荐分值计算完成{}", user.getNickname());
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        return finalResultMap;
    }
}
