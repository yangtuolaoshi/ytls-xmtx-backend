package love.ytlsnb.quest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import love.ytlsnb.common.utils.UserHolder;
import love.ytlsnb.model.quest.po.ClockInLog;
import love.ytlsnb.model.quest.po.QuestSchedule;
import love.ytlsnb.model.quest.po.QuestScheduleLog;
import love.ytlsnb.quest.mapper.ClockInLogMapper;
import love.ytlsnb.quest.mapper.QuestScheduleLogMapper;
import love.ytlsnb.quest.mapper.QuestScheduleMapper;
import love.ytlsnb.quest.service.QuestLogService;
import love.ytlsnb.quest.service.QuestScheduleClockInMethodService;
import love.ytlsnb.quest.service.QuestScheduleLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * 任务进度完成记录业务层实现类
 *
 * @author 金泓宇
 * @date 2024/3/15
 */
@Service
public class QuestScheduleLogServiceImpl implements QuestScheduleLogService {
    @Autowired
    private QuestScheduleMapper questScheduleMapper;

    @Autowired
    private QuestScheduleLogMapper questScheduleLogMapper;

    @Autowired
    private ClockInLogMapper clockInLogMapper;

    @Autowired
    private QuestScheduleClockInMethodService questScheduleClockInMethodService;

    @Autowired
    private QuestLogService questLogService;

    @Transactional
    @Override
    public Boolean isCompleted(Long questScheduleId) {
        Long userId = UserHolder.getUser().getId();
        // 查询这个进度所需的打卡方式
        List<Long> clockInMethodIds = questScheduleClockInMethodService.getClockInMethodIdsByQuestScheduleId(questScheduleId);
        // 查询当前用户该进度的打卡记录
        LambdaQueryWrapper<ClockInLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(ClockInLog::getUserId, userId)
                .eq(ClockInLog::getQuestScheduleId, questScheduleId);
        List<ClockInLog> clockInLogs = clockInLogMapper.selectList(queryWrapper);
        LinkedList<Long> finishedClockInMethodIds = new LinkedList<>();
        clockInLogs.forEach((clockInLog -> {
            Long finishedClockInMethodId = clockInLog.getClockInMethodId();
            finishedClockInMethodIds.add(finishedClockInMethodId);
        }));
        clockInMethodIds.removeAll(finishedClockInMethodIds);
        // 还有没打卡的方式
        if (clockInMethodIds.size() > 0) {
            return false;
        } else {
            // 打卡完成了，就添加一条进度完成记录
            QuestScheduleLog questScheduleLog = new QuestScheduleLog();
            questScheduleLog.setQuestScheduleId(questScheduleId);
            questScheduleLog.setUserId(userId);
            questScheduleLog.setCreateTime(LocalDateTime.now());
            questScheduleLogMapper.insert(questScheduleLog);
            // 进度完成后，再检查任务有没有完成
            QuestSchedule questSchedule = questScheduleMapper.selectById(questScheduleId);
            Long questId = questSchedule.getQuestId();
            questLogService.isCompleted(questId);
            return true;
        }
    }
}
