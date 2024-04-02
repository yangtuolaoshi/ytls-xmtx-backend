package love.ytlsnb.reward.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.reward.dto.RewardDTO;
import love.ytlsnb.model.reward.dto.RewardPhotoDTO;
import love.ytlsnb.model.reward.dto.RewardQueryDTO;
import love.ytlsnb.model.reward.po.Reward;
import love.ytlsnb.model.reward.po.RewardPhoto;
import love.ytlsnb.model.reward.vo.RewardVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author ula
 * @date 2024/2/6 14:23
 */
public interface RewardService extends IService<Reward> {
    void add(RewardDTO rewardDTO);

    PageResult<List<RewardVO>> getPageByCondition(RewardQueryDTO rewardQueryDTO, int page, int size);

    Boolean update(RewardDTO rewardDTO);

    void deleteWithPhotoById(Long id);

    List<Reward> selectBySchoolId(Long schoolId);

    void deleteByPhoto(RewardPhotoDTO rewardPhotoDTO);

    String upload(MultipartFile file);
}
