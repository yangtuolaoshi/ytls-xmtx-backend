package love.ytlsnb.quest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import love.ytls.api.user.UserClient;
import love.ytlsnb.common.utils.UserHolder;
import love.ytlsnb.model.quest.po.Quest;
import love.ytlsnb.model.quest.po.QuestLog;
import love.ytlsnb.model.quest.po.QuestSchedule;
import love.ytlsnb.model.quest.po.QuestScheduleLog;
import love.ytlsnb.quest.mapper.QuestLogMapper;
import love.ytlsnb.quest.mapper.QuestMapper;
import love.ytlsnb.quest.mapper.QuestScheduleLogMapper;
import love.ytlsnb.quest.mapper.QuestScheduleMapper;
import love.ytlsnb.quest.service.QuestLogService;
import love.ytlsnb.quest.service.QuestScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * 任务完成情况业务层实现类
 *
 * @author 金泓宇
 * @date 2024/3/15
 */
@Service
public class QuestLogServiceImpl implements QuestLogService {
    @Autowired
    private QuestMapper questMapper;

    @Autowired
    private QuestLogMapper questLogMapper;

    @Autowired
    private QuestScheduleMapper questScheduleMapper;

    @Autowired
    private QuestScheduleLogMapper questScheduleLogMapper;

    @Autowired
    private UserClient userClient;

    @Transactional
    @Override
    public Boolean isCompleted(Long questId) {
        Long userId = UserHolder.getUser().getId();
        // 查询任务下的所有进度
        LambdaQueryWrapper<QuestSchedule> questScheduleQueryWrapper = new LambdaQueryWrapper<>();
        questScheduleQueryWrapper.eq(QuestSchedule::getQuestId, questId);
        List<QuestSchedule> questSchedules = questScheduleMapper.selectList(questScheduleQueryWrapper);
        // 查询当前用户该任务的进度完成情况
        LinkedList<Long> questScheduleIds = new LinkedList<>();
        questSchedules.forEach(questSchedule -> {
            Long questScheduleId = questSchedule.getId();
            questScheduleIds.add(questScheduleId);
        });
        LambdaQueryWrapper<QuestScheduleLog> questScheduleLogQueryWrapper = new LambdaQueryWrapper<>();
        questScheduleLogQueryWrapper
                .eq(QuestScheduleLog::getUserId, userId)
                .in(QuestScheduleLog::getQuestScheduleId, questScheduleIds);
        List<QuestScheduleLog> questScheduleLogs = questScheduleLogMapper.selectList(questScheduleLogQueryWrapper);
        // 查询该任务要求的进度完成数
        Quest quest = questMapper.selectById(questId);
        Integer requiredScheduleNum = quest.getRequiredScheduleNum();
        // 看看已完成的进度数是否达到任务完成要求的进度数
        int finishedScheduleNum = questScheduleLogs.size();
        if (finishedScheduleNum >= requiredScheduleNum) {
            // 达到要求了就添加一个记录
            // TODO 考虑一下是否还需要左右值
            QuestLog questLog = new QuestLog();
            questLog.setQuestId(questId);
            questLog.setUserId(userId);
            questLog.setCreateTime(LocalDateTime.now());
            questLogMapper.insert(questLog);
            // 给用户加积分
            Integer reward = quest.getReward();
            userClient.addPoint(reward);
            return true;
        } else {
            return false;
        }
    }
}
