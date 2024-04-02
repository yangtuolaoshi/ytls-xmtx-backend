package love.ytlsnb.ad.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.ytlsnb.model.ad.po.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ula
 * @date 2024/3/20 14:45
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    List<Tag> listAllTagsByAdvertisementId(Long adId);

    List<Tag> listAllTagsByUserId(Long userId);

    @Select("select * from tb_tag where id = parent_id")
    List<Tag> ListParents();
}
