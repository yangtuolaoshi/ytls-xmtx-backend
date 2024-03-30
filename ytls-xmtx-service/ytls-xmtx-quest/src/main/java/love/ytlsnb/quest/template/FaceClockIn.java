package love.ytlsnb.quest.template;

import love.ytlsnb.quest.mapper.ClockInLogMapper;
import love.ytlsnb.quest.service.QuestScheduleLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 人脸打卡实现类
 *
 * @author 金泓宇
 * @date 2024/3/15
 */
@Component("faceClockIn")
public class FaceClockIn extends ClockInTemplate<MultipartFile> {
    @Autowired
    public FaceClockIn(ClockInLogMapper clockInLogMapper, QuestScheduleLogService questScheduleLogService) {
        super(clockInLogMapper, questScheduleLogService, 3L);
    }

    @Override
    protected boolean clockInTest(Long questScheduleId, MultipartFile photo) {
        // TODO 实现真正的人脸打卡
        return true;
    }
}
