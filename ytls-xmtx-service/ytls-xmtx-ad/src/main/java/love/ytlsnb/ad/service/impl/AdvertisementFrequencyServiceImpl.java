package love.ytlsnb.ad.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.ad.mapper.AdvertisementFrequencyMapper;
import love.ytlsnb.ad.service.AdvertisementFrequencyService;
import love.ytlsnb.model.ad.po.AdvertisementFrequency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * @author ula
 * @date 2024/3/31 15:29
 */
@Slf4j
@Service
public class AdvertisementFrequencyServiceImpl extends ServiceImpl<AdvertisementFrequencyMapper, AdvertisementFrequency> implements AdvertisementFrequencyService {
    @Autowired
    private AdvertisementFrequencyMapper adFrequencyMapper;

    @Override
    public List<AdvertisementFrequency> listTodaysByUserId(Long userId) {
        return lambdaQuery().eq(AdvertisementFrequency::getUserId, userId)
                .ge(AdvertisementFrequency::getCreateTime, LocalDateTime.of(LocalDate.now(), LocalTime.MIN))
                .list();
    }

    @Override
    @Transactional
    public void increaseByAdIdAndUserId(Long adId, Long userId) {
        AdvertisementFrequency adFrequency = lambdaQuery().eq(AdvertisementFrequency::getAdvertisementId, adId)
                .eq(AdvertisementFrequency::getUserId, userId)
                .one();
        if (adFrequency == null) {
            save(AdvertisementFrequency.builder()
                    .advertisementId(adId)
                    .userId(userId)
                    .frequency(1)
                    .build());
        } else {
            // 使用链式Wrapper中的set修改数据不会进行相关参数的自动注入，这里需要手动设置修改时间
            lambdaUpdate().eq(AdvertisementFrequency::getAdvertisementId, adId)
                    .eq(AdvertisementFrequency::getUserId, userId)
                    .set(AdvertisementFrequency::getFrequency, adFrequency.getFrequency() + 1)
                    .set(AdvertisementFrequency::getUpdateTime, LocalDateTime.now())
                    .update();
        }
    }
}
