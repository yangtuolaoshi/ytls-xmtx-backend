package love.ytlsnb.quest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.model.quest.po.ClockInMethod;
import love.ytlsnb.model.quest.po.QuestScheduleClockInMethod;
import love.ytlsnb.quest.mapper.ClockInMethodMapper;
import love.ytlsnb.quest.mapper.QuestScheduleClockInMethodMapper;
import love.ytlsnb.quest.service.QuestScheduleClockInMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static love.ytlsnb.common.constants.ResultCodes.SERVER_ERROR;

/**
 * 任务进度-打卡方式关联业务层实现类
 *
 * @author 金泓宇
 * @date 2024/3/14
 */
@Service
@Slf4j
public class QuestScheduleClockInMethodServiceImpl implements QuestScheduleClockInMethodService {
    @Autowired
    private ClockInMethodMapper clockInMethodMapper;

    @Autowired
    private QuestScheduleClockInMethodMapper questScheduleClockInMethodMapper;

    @Transactional
    @Override
    public Boolean addClockInMethod2Schedule(Long questScheduleId, List<Long> clockInMethodIds) {
        // TODO 批量添加优化
//        LinkedList<QuestScheduleClockInMethod> questScheduleClockInMethods = new LinkedList<>();
        clockInMethodIds.forEach(clockInMethodId -> {
            QuestScheduleClockInMethod questScheduleClockInMethod = new QuestScheduleClockInMethod();
            questScheduleClockInMethod.setQuestScheduleId(questScheduleId);
            questScheduleClockInMethod.setClockInMethodId(clockInMethodId);
            questScheduleClockInMethod.setCreateTime(LocalDateTime.now());
            questScheduleClockInMethod.setIsDeleted(0);
            if (questScheduleClockInMethodMapper.insert(questScheduleClockInMethod) == 0) {
                log.error("添加打卡方式失败: {}", questScheduleId);
                throw new BusinessException(SERVER_ERROR, "添加打卡方式失败！");
            }
//            questScheduleClockInMethods.add(questScheduleClockInMethod);
        });
        return true;
    }

    @Transactional
    @Override
    public Boolean deleteByQuestScheduleId(Long questScheduleId) {
        LambdaQueryWrapper<QuestScheduleClockInMethod> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuestScheduleClockInMethod::getQuestScheduleId, questScheduleId);
        return questScheduleClockInMethodMapper.delete(queryWrapper) > 0;
    }

    @Transactional
    @Override
    public Boolean deleteByQuestScheduleIds(List<Long> questScheduleIds) {
        LambdaQueryWrapper<QuestScheduleClockInMethod> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(QuestScheduleClockInMethod::getQuestScheduleId, questScheduleIds);
        return questScheduleClockInMethodMapper.delete(queryWrapper) > 0;
    }

    @Override
    public List<Long> getClockInMethodIdsByQuestScheduleId(Long questScheduleId) {
        // 查询关联
        LambdaQueryWrapper<QuestScheduleClockInMethod> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuestScheduleClockInMethod::getQuestScheduleId, questScheduleId);
        List<QuestScheduleClockInMethod> questScheduleClockInMethods = questScheduleClockInMethodMapper.selectList(queryWrapper);
        // 从关联里获取打卡方式ID
        LinkedList<Long> clockInMethodIds = new LinkedList<>();
        questScheduleClockInMethods.forEach(questScheduleClockInMethod -> {
            Long clockInMethodId = questScheduleClockInMethod.getClockInMethodId();
            clockInMethodIds.add(clockInMethodId);
        });
        return clockInMethodIds;
    }

    @Override
    public List<ClockInMethod> getClockInMethodsByQuestScheduleId(Long questScheduleId) {
        List<Long> clockInMethodIds = this.getClockInMethodIdsByQuestScheduleId(questScheduleId);
        return clockInMethodMapper.selectBatchIds(clockInMethodIds);
    }
}
