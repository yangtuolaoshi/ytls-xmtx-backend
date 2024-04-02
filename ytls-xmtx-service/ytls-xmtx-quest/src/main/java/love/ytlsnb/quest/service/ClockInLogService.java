package love.ytlsnb.quest.service;

import love.ytlsnb.model.quest.dto.LocationCheckDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 打卡记录业务层接口
 *
 * @author 金泓宇
 * @date 2024/3/15
 */
public interface ClockInLogService {
    /**
     * 地点打卡
     * @param locationCheckDTO 地点打卡条件
     * @param questScheduleId 进度ID
     * @return 是否成功
     */
    Boolean locationCheck(LocationCheckDTO locationCheckDTO, Long questScheduleId);

    /**
     * 图片打卡
     * @param questScheduleId 进度ID
     * @return 是否成功
     */
    Boolean photoCheck(Long questScheduleId, MultipartFile photo);

    /**
     * 人脸打卡
     * @param questScheduleId 进度ID
     * @return 是否成功
     */
    Boolean faceCheck(Long questScheduleId);

    /**
     * 管理员发卡
     * @param questScheduleId 进度ID
     * @return 是否成功
     */
    Boolean adminCheck(Long questScheduleId);
}
