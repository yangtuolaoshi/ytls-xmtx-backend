package love.ytlsnb.quest.template;

import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.quest.mapper.ClockInLogMapper;
import love.ytlsnb.quest.service.QuestScheduleLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import static love.ytlsnb.common.constants.ResultCodes.UNPROCESSABLE_ENTITY;

/**
 * 照片打卡实现类
 *
 * @author 金泓宇
 * @date 2024/3/15
 */
@Component("photoClockIn")
public class PhotoClockIn extends ClockInTemplate<MultipartFile> {
    @Autowired
    public PhotoClockIn(ClockInLogMapper clockInLogMapper, QuestScheduleLogService questScheduleLogService) {
        super(clockInLogMapper, questScheduleLogService, 2L);
    }

    @Override
    protected boolean clockInTest(Long questScheduleId, MultipartFile photo) {
        // TODO 实现真正的照片打卡
        if (photo == null) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "打卡参数错误");
        }
        System.out.println(photo.getSize());
        return true;
    }
}
