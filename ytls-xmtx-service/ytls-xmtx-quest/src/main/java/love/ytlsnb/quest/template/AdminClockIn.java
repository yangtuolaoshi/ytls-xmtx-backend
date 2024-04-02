package love.ytlsnb.quest.template;

import love.ytlsnb.quest.mapper.ClockInLogMapper;
import love.ytlsnb.quest.service.QuestScheduleLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 管理员打卡
 *
 * @author 金泓宇
 * @date 2024/3/15
 */
@Component("adminClockIn")
public class AdminClockIn extends ClockInTemplate<Object> {
    @Autowired
    public AdminClockIn(ClockInLogMapper clockInLogMapper, QuestScheduleLogService questScheduleLogService) {
        super(clockInLogMapper, questScheduleLogService, 4L);
    }

    @Override
    protected boolean clockInTest(Long questScheduleId, Object object) {
        // TODO 实现真正的管理员打卡
        return true;
    }
}
