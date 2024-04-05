package love.ytlsnb.quest.template;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.utils.UserHolder;
import love.ytlsnb.model.quest.po.ClockInLog;
import love.ytlsnb.quest.mapper.ClockInLogMapper;
import love.ytlsnb.quest.service.QuestScheduleLogService;

import java.time.LocalDateTime;
import java.util.List;

import static love.ytlsnb.common.constants.ResultCodes.NOT_ACCEPTABLE;

/**
 * 打卡模板方法抽象类
 *
 * @author 金泓宇
 * @date 2024/3/15
 */
@Slf4j
public abstract class ClockInTemplate<T> {
    protected ClockInLogMapper clockInLogMapper;

    protected QuestScheduleLogService questScheduleLogService;

    /**
     * 打卡方式ID
     */
    protected Long clockInMethodId;

    // 抽象类不能实例化，因此不能注入成员，通过实现类调用父类构造函数来注入
    public ClockInTemplate(ClockInLogMapper clockInLogMapper, QuestScheduleLogService questScheduleLogService, Long clockInMethodId) {
        this.clockInLogMapper = clockInLogMapper;
        this.questScheduleLogService = questScheduleLogService;
        this.clockInMethodId = clockInMethodId;
    }

    /**
     * 是否已经打卡过了
     */
    protected void isChecked(Long questScheduleId) {
        Long userId = UserHolder.getUser().getId();
        LambdaQueryWrapper<ClockInLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(ClockInLog::getUserId, userId)
                .eq(ClockInLog::getQuestScheduleId, questScheduleId)
                .eq(ClockInLog::getClockInMethodId, clockInMethodId);
        List<ClockInLog> clockInLogs = clockInLogMapper.selectList(queryWrapper);
        if (clockInLogs.size() > 0) {
            throw new BusinessException(NOT_ACCEPTABLE, "您已经打卡过了");
        }
    }

    /**
     * 不同的打卡方式检测逻辑不同
     * @return 检测打卡是否成功
     */
    /*
    不同的打卡方式需要的参数不同，有两种解决方案
    1. 泛型（或者直接Object类型）
    2. 抽象工厂模式
     */
    protected abstract boolean clockInTest(Long questScheduleId, T param);

    /**
     * 添加打卡记录
     */
    protected void addClockInLog(Long questScheduleId) {
        Long userId = UserHolder.getUser().getId();
        // 添加打卡记录
        ClockInLog clockInLog = new ClockInLog();
        clockInLog.setUserId(userId);
        clockInLog.setQuestScheduleId(questScheduleId);
        clockInLog.setClockInMethodId(this.clockInMethodId);
        clockInLog.setCreateTime(LocalDateTime.now());
        clockInLogMapper.insert(clockInLog);
    }

    /**
     * 进度是否完成
     */
    protected void isScheduleCompleted(Long questScheduleId) {
        // 查询进度是否完成
        questScheduleLogService.isCompleted(questScheduleId);
    }

    /**
     * 模板方法：打卡的整个过程
     */
    public boolean clockIn(Long questScheduleId, T clockInParam) {
        // TODO 加锁防止重复打卡
        // 是否已经打卡
        isChecked(questScheduleId);
        // 检测打卡条件
        boolean isCheckSucceed = clockInTest(questScheduleId, clockInParam);
        //  如果打卡条件判断成功
        if (isCheckSucceed) {
            // 添加一条打卡记录
            addClockInLog(questScheduleId);
            // 看看这次打卡完这个进度是否能完成
            isScheduleCompleted(questScheduleId);
            return true;
        } else {
            return false;
        }
    }
}
