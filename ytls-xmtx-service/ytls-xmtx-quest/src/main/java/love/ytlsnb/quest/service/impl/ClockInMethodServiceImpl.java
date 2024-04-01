package love.ytlsnb.quest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import love.ytlsnb.model.quest.po.ClockInMethod;
import love.ytlsnb.model.quest.po.QuestScheduleClockInMethod;
import love.ytlsnb.quest.mapper.ClockInMethodMapper;
import love.ytlsnb.quest.mapper.QuestScheduleClockInMethodMapper;
import love.ytlsnb.quest.service.ClockInMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * 打卡方式业务层实现类
 *
 * @author 金泓宇
 * @date 2024/3/14
 */
@Service
public class ClockInMethodServiceImpl implements ClockInMethodService {
    @Autowired
    private ClockInMethodMapper clockInMethodMapper;

    @Autowired
    private QuestScheduleClockInMethodMapper questScheduleClockInMethodMapper;

    @Override
    public List<ClockInMethod> getAll() {
        return clockInMethodMapper.selectList(null);
    }

    @Override
    public List<ClockInMethod> getByQuestScheduleId(Long questScheduleId) {
        LambdaQueryWrapper<QuestScheduleClockInMethod> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuestScheduleClockInMethod::getQuestScheduleId, questScheduleId);
        List<QuestScheduleClockInMethod> questScheduleClockInMethods = questScheduleClockInMethodMapper.selectList(queryWrapper);
        List<Long> clockInMethodIds = new LinkedList<>();
        questScheduleClockInMethods.forEach(questScheduleClockInMethod -> {
            Long clockInMethodId = questScheduleClockInMethod.getClockInMethodId();
            clockInMethodIds.add(clockInMethodId);
        });
        return clockInMethodMapper.selectBatchIds(clockInMethodIds);
    }
}
