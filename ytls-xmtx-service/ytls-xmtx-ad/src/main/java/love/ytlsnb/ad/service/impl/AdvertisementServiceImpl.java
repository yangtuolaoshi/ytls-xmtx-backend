package love.ytlsnb.ad.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.ad.mapper.AdvertisementMapper;
import love.ytlsnb.ad.service.*;
import love.ytlsnb.common.constants.AdvertisementConstant;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.model.ad.dto.AdvertisementInsertDTO;
import love.ytlsnb.model.ad.dto.AdvertisementQueryDTO;
import love.ytlsnb.model.ad.po.*;
import love.ytlsnb.model.ad.vo.AdvertisementVO;
import love.ytlsnb.model.common.Result;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ula
 * @date 2024/3/20 14:30
 */
@Slf4j
@Service
public class AdvertisementServiceImpl extends ServiceImpl<AdvertisementMapper, Advertisement> implements AdvertisementService {
    @Autowired
    private AdvertisementMapper adMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private AdvertisementTagService adTagService;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private RecommendationScoreService recommendationScoreService;
    @Autowired
    private AdvertisementFrequencyService adFrequencyService;

    @Override
    public List<Advertisement> listBySharding(int shardTotal, int shardIndex) {
        return adMapper.listBySharding(shardTotal, shardIndex);
    }

    @Override
    @Transactional
    public void addAdvertisement(AdvertisementInsertDTO adInsertDTO) {
        Advertisement ad = BeanUtil.copyProperties(adInsertDTO, Advertisement.class);
        List<Long> tagIdList = adInsertDTO.getTagIdList();
        Boolean eligible = tagService.checkTagIdList(tagIdList);
        if (!eligible) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "请添加正确的标签");
        }
        adMapper.insert(ad);
        List<AdvertisementTag> adTagList = BeanUtil.copyToList(tagIdList, AdvertisementTag.class);
        adTagList.forEach(adTag -> adTag.setAdvertisementId(ad.getId()));
        adTagService.saveBatch(adTagList);
    }

    @Override
    @Transactional
    public void deleteAdvertisementById(Long adId) {
        // 删除广告
        adMapper.deleteById(adId);
        // 向消息队列发送删除广告的消息
        amqpTemplate.convertAndSend(AdvertisementConstant.AD_DELETE_FANOUT_EXCHANGE_NAME, "", adId);
    }

    @Override
    public void updateAdvertisement(AdvertisementInsertDTO adUpdateDTO) {
        Advertisement ad = BeanUtil.copyProperties(adUpdateDTO, Advertisement.class);
        List<Long> tagIdList = adUpdateDTO.getTagIdList();
        Boolean eligible = tagService.checkTagIdList(tagIdList);
        if (!eligible) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "请添加正确的标签");
        }
        adMapper.updateById(ad);
        List<AdvertisementTag> adTagList = BeanUtil.copyToList(tagIdList, AdvertisementTag.class);
        adTagList.forEach(adTag -> adTag.setAdvertisementId(ad.getId()));
        // 删除已有标签
        adTagService.remove(new LambdaQueryWrapper<AdvertisementTag>()
                .eq(AdvertisementTag::getAdvertisementId, ad.getId()));
        // 新增标签
        adTagService.saveBatch(adTagList);
    }

    @Override
    public AdvertisementVO getVOById(Long adId) {
        // 查询广告信息
        Advertisement ad = adMapper.selectById(adId);
        if (ad == null) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "请输入正确的广告ID");
        }
        // 查询相关标签信息
        List<Tag> tagList = tagService.listAllByAdvertisementId(adId);
        // 封装数据进行返回
        AdvertisementVO adVO = BeanUtil.copyProperties(ad, AdvertisementVO.class);
        adVO.setTagList(tagList);
        return adVO;
    }

    @Override
    public List<AdvertisementVO> getPageByConditions(AdvertisementQueryDTO adQueryDTO) {
        Page<Advertisement> page = new Page<>();
        page.setCurrent(adQueryDTO.getCurrentPage());
        page.setSize(adQueryDTO.getPageSize());
        lambdaQuery().likeRight(Advertisement::getDescription, adQueryDTO.getDescription())
                .likeRight(Advertisement::getCustomerName, adQueryDTO.getCustomerName())
                .likeRight(Advertisement::getCustomerPhone, adQueryDTO.getCustomerPhone())
                .page(page);
        List<AdvertisementVO> adVOList = BeanUtil.copyToList(page.getRecords(), AdvertisementVO.class);
        adVOList.forEach(adVO -> adVO.setTagList(tagService.listAllByAdvertisementId(adVO.getId())));
        return adVOList;
    }

    @Override
    public List<Advertisement> list2User(Long userId, Integer size) {
        // 根据用户查询出所有广告的推荐得分
        Map<Long, BigDecimal> adRecommendationScoreMap = recommendationScoreService.listByUserId(userId).stream()
                .collect(Collectors.toMap(RecommendationScore::getAdvertisementId, RecommendationScore::getScore));
        // 查询所有广告的当日推送频次
        Map<Long, Integer> adFrequencyMap = adFrequencyService.listTodaysByUserId(userId).stream()
                .collect(Collectors.toMap(AdvertisementFrequency::getAdvertisementId, AdvertisementFrequency::getFrequency));
        // 根据广告-用户当日推送频次重新计算广告用户推荐得分
        for (Map.Entry<Long, BigDecimal> entry : adRecommendationScoreMap.entrySet()) {
            Integer frequency = adFrequencyMap.get(entry.getKey());
            BigDecimal weight = new BigDecimal(frequency == null ? 1 : frequency);
            entry.setValue(entry.getValue()
                    .divide(weight, AdvertisementConstant.DEFAULT_BIGDECIMAL_SCALE, RoundingMode.HALF_UP));
        }
        // 根据重新计算出的推荐得分进行排序，并重新封装广告集合对象
        // 下面第一个集合是用来Debug的
        List<Map.Entry<Long, BigDecimal>> sortedEntryList = adRecommendationScoreMap.entrySet().stream()
                .sorted(((o1, o2) -> o2.getValue().compareTo(o1.getValue()))).toList();
        List<Long> sortedAdIdList = adRecommendationScoreMap.entrySet().stream()
                .sorted(((o1, o2) -> o2.getValue().compareTo(o1.getValue()))).map(Map.Entry::getKey).toList();
        // 结果集
        List<Advertisement> resList = new ArrayList<>();
        for (int i = 0; i < sortedAdIdList.size() && size > 0; i++) {
            // 根据广告ID获取广告（广告若已到期返回null）
            Advertisement ad = lambdaQuery().eq(Advertisement::getId, sortedAdIdList.get(i))
                    .gt(Advertisement::getExpirationTime, LocalDateTime.now())
                    .one();
            if (ad != null) {
                resList.add(ad);
                size--;
                // 对应广告的推荐频次加一
                adFrequencyService.increaseByAdIdAndUserId(ad.getId(), userId);
            }
        }
        // TODO 如果没有可推荐的广告，可以再次添加默认广告
        // 返回广告集合
        return resList;
    }

}
