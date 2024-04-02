package love.ytlsnb.reward.mapper;
import love.ytlsnb.model.reward.po.RewardPhoto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author QiaoQiao
 * @date 2024/3/20 21:17
 */
@Mapper
public interface RewardPhotoMapper {
    @Delete("delete from tb_reward_photo where reward_id = #{rewardId}")
    Boolean deleteById(Long rewardId);

    @Select("select * from tb_reward_photo where reward_id = #{rewardid}")
    List<RewardPhoto> getByRewardId(String rewardId);

    void insert(List<RewardPhoto> rewardPhotos);

    @Insert("insert into tb_reward_photo (cover) values #{cover}")
    void insertCover(String cover);

}
