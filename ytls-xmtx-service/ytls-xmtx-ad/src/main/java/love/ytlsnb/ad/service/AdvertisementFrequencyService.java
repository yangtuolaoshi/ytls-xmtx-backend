package love.ytlsnb.ad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.ytlsnb.model.ad.po.AdvertisementFrequency;

import java.util.List;

/**
 * @author ula
 * @date 2024/3/31 15:28
 */
public interface AdvertisementFrequencyService extends IService<AdvertisementFrequency> {
    List<AdvertisementFrequency> listTodaysByUserId(Long userId);

    void increaseByAdIdAndUserId(Long id, Long userId);
}
