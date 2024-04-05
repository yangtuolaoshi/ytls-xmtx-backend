package love.ytlsnb.ad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.ytlsnb.model.ad.po.AdvertisementSimilarity;

import java.util.List;

/**
 * @author ula
 * @date 2024/3/20 14:40
 */
public interface AdvertisementSimilarityService extends IService<AdvertisementSimilarity> {
    List<Long> listSimilarAdIdByAdId(Long id);

    List<AdvertisementSimilarity> listBySimilarAdId(Long adId);
}
