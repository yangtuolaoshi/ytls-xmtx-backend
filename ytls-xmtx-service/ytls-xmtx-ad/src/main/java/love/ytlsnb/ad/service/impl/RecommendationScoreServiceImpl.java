package love.ytlsnb.ad.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.ad.mapper.RecommendationScoreMapper;
import love.ytlsnb.ad.service.RecommendationScoreService;
import love.ytlsnb.model.ad.po.RecommendationScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @author ula
 * @date 2024/3/23 16:02
 */
@Slf4j
@Service
public class RecommendationScoreServiceImpl extends ServiceImpl<RecommendationScoreMapper, RecommendationScore> implements RecommendationScoreService {
    @Autowired
    private RecommendationScoreMapper recommendationScoreMapper;

    @Override
    @Transactional
    public boolean saveOrUpdateBatch(Collection<RecommendationScore> entityList, int batchSize) {
        boolean success = true;
        for (RecommendationScore rs : entityList) {
            RecommendationScore select = getOne(new LambdaQueryWrapper<RecommendationScore>()
                    .eq(RecommendationScore::getUserId, rs.getUserId())
                    .eq(RecommendationScore::getAdvertisementId, rs.getAdvertisementId()));
            if (select == null) {
                recommendationScoreMapper.insert(rs);
            } else {
                success &= lambdaUpdate().eq(RecommendationScore::getUserId, rs.getUserId())
                        .eq(RecommendationScore::getAdvertisementId, rs.getAdvertisementId())
                        .set(RecommendationScore::getScore, rs.getScore())
                        .update();
            }
        }
        return success;
    }

    @Override
    public List<RecommendationScore> listByUserId(Long userId) {
        return lambdaQuery().eq(RecommendationScore::getUserId, userId)
                .list();
    }
}
