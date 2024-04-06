package love.ytlsnb.ad.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.ytlsnb.model.ad.po.AdvertisementSimilarity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ula
 * @date 2024/3/20 14:39
 */
@Mapper
public interface AdvertisementSimilarityMapper extends BaseMapper<AdvertisementSimilarity> {
    @Select("select similar_advertisement_id from tb_advertisement_similarity where advertisement_id = #{adId} and similarity > 0 and is_deleted = 0")
    List<Long> listSimilarAdIdByAdId(Long adId);

    @Select("select * from tb_advertisement_similarity where similar_advertisement_id = #{adId} and is_deleted = 0")
    List<AdvertisementSimilarity> listBySimilarAdId(Long adId);
}
