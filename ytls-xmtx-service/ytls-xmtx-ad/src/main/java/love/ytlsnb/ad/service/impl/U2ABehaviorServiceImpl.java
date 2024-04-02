package love.ytlsnb.ad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.ad.mapper.U2ABehaviorMapper;
import love.ytlsnb.ad.service.U2ABehaviorService;
import love.ytlsnb.common.constants.AdvertisementConstant;
import love.ytlsnb.model.ad.dto.UserBehaviorScoreDTO;
import love.ytlsnb.model.ad.po.U2ABehavior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ula
 * @date 2024/3/20 14:57
 */
@Slf4j
@Service
public class U2ABehaviorServiceImpl extends ServiceImpl<U2ABehaviorMapper, U2ABehavior> implements U2ABehaviorService {
    @Autowired
    private U2ABehaviorMapper u2aBehaviorMapper;

    @Override
    public List<Long> listLikedAdIdsByUserId(Long userId) {
        return u2aBehaviorMapper.listLikedAdIdsByUserId(userId);
    }

    @Override
    public BigDecimal getAdSumScoreByAdId(Long adId) {
        return u2aBehaviorMapper.getAdSumScoreByAdId(adId);
    }

    @Override
    public BigDecimal getScoreByUserIdAndAdId(Long userId, Long adId) {
        return u2aBehaviorMapper.getScoreByUserIdAndAdId(userId, adId);
    }

    @Override
    public List<UserBehaviorScoreDTO> listScoreByUserId(Long userId) {
        return u2aBehaviorMapper.listScoreByUserId(userId);
    }

    @Override
    public List<UserBehaviorScoreDTO> listScoreByAdId(Long adId) {
        return u2aBehaviorMapper.listScoreByAdId(adId);
    }

}
