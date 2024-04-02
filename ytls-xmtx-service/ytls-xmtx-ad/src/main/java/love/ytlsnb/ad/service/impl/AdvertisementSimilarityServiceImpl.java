package love.ytlsnb.ad.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.ad.mapper.AdvertisementSimilarityMapper;
import love.ytlsnb.ad.service.AdvertisementSimilarityService;
import love.ytlsnb.model.ad.po.AdvertisementSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @author ula
 * @date 2024/3/20 14:40
 */
@Slf4j
@Service
public class AdvertisementSimilarityServiceImpl extends ServiceImpl<AdvertisementSimilarityMapper, AdvertisementSimilarity> implements AdvertisementSimilarityService {
    @Autowired
    private AdvertisementSimilarityMapper adSimilarityMapper;

    @Override
    public List<Long> listSimilarAdIdByAdId(Long adId) {
        return adSimilarityMapper.listSimilarAdIdByAdId(adId);
    }

    @Override
    @Transactional
    public boolean saveOrUpdateBatch(Collection<AdvertisementSimilarity> entityList, int batchSize) {
        boolean success = true;
        for (AdvertisementSimilarity ads : entityList) {
            AdvertisementSimilarity select = lambdaQuery().eq(AdvertisementSimilarity::getAdvertisementId, ads.getSimilarAdvertisementId())
                    .eq(AdvertisementSimilarity::getSimilarAdvertisementId, ads.getSimilarAdvertisementId())
                    .one();
            if (select == null) {
                adSimilarityMapper.insert(ads);
            } else {
                success &= lambdaUpdate().eq(AdvertisementSimilarity::getAdvertisementId, ads.getSimilarAdvertisementId())
                        .eq(AdvertisementSimilarity::getSimilarAdvertisementId, ads.getSimilarAdvertisementId())
                        .set(AdvertisementSimilarity::getSimilarity, ads.getSimilarity())
                        .update();
            }
        }
        return success;
    }

    @Override
    public List<AdvertisementSimilarity> listBySimilarAdId(Long adId) {
        return adSimilarityMapper.listBySimilarAdId(adId);
    }
}
