package love.ytlsnb.reward.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.ytlsnb.model.reward.dto.RewardQueryDTO;
import love.ytlsnb.model.reward.po.Reward;
import love.ytlsnb.model.reward.po.RewardPhoto;
import love.ytlsnb.model.reward.vo.RewardVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ula
 * @date 2024/2/6 14:26
 */
@Mapper
public interface RewardMapper extends BaseMapper<Reward> {
    List<RewardVO> getPageByCondition(RewardQueryDTO rewardQueryDTO, int i, int size);

    @Select("")
//todo
    void one(LambdaQueryWrapper<RewardPhoto> lambdaQueryWrapper);

    @Delete("delete from tb_reward_photo where id = #{RewardPhoto.id}")
    void deleteByPhotoId(Long photoId);

    void updateStock(Long id, int stock);

    /**
     * 删除奖品信息以及图片
     * @param id
     * @return
     */

}
