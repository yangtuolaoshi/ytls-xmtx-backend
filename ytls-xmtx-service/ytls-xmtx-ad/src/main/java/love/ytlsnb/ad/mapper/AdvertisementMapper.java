package love.ytlsnb.ad.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.ytlsnb.model.ad.po.Advertisement;
import love.ytlsnb.model.common.Result;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ula
 * @date 2024/3/20 14:29
 */
@Mapper
public interface AdvertisementMapper extends BaseMapper<Advertisement> {
    @Select("select * from tb_advertisement where mod(id,#{total})=#{index}) and is_deleted = 0")
    List<Advertisement> listBySharding(int shardTotal, int shardIndex);
}
