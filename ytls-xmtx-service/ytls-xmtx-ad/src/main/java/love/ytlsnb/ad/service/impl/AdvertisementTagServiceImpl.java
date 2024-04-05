package love.ytlsnb.ad.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.ytlsnb.ad.mapper.AdvertisementTagMapper;
import love.ytlsnb.ad.service.AdvertisementTagService;
import love.ytlsnb.model.ad.po.AdvertisementTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ula
 * @date 2024/3/29 15:48
 */
@Service
public class AdvertisementTagServiceImpl extends ServiceImpl<AdvertisementTagMapper, AdvertisementTag> implements AdvertisementTagService {
    @Autowired
    private AdvertisementTagMapper adTagMapper;
}
