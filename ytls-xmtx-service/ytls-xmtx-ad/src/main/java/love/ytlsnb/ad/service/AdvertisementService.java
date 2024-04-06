package love.ytlsnb.ad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.ytlsnb.model.ad.dto.AdvertisementInsertDTO;
import love.ytlsnb.model.ad.dto.AdvertisementQueryDTO;
import love.ytlsnb.model.ad.po.Advertisement;
import love.ytlsnb.model.ad.vo.AdvertisementVO;
import love.ytlsnb.model.common.Result;

import java.util.List;

/**
 * @author ula
 * @date 2024/3/20 14:29
 */
public interface AdvertisementService extends IService<Advertisement> {
    List<Advertisement> listBySharding(int shardTotal, int shardIndex);

    void addAdvertisement(AdvertisementInsertDTO adInsertDTO);

    void deleteAdvertisementById(Long adId);

    void updateAdvertisement(AdvertisementInsertDTO adUpdateDTO);

    AdvertisementVO getVOById(Long adId);

    List<AdvertisementVO> getPageByConditions(AdvertisementQueryDTO adQueryDTO);

    List<Advertisement> list2User(Long userId, Integer size);
}
