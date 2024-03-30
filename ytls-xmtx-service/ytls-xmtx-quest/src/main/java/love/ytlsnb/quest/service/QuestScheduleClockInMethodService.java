package love.ytlsnb.quest.service;

import love.ytlsnb.model.quest.po.ClockInMethod;

import java.util.List;

/**
 * 任务进度-打卡方式关联业务层接口
 *
 * @author 金泓宇
 * @date 2024/3/14
 */
public interface QuestScheduleClockInMethodService {
    /**
     * 为进度添加打卡方式
     * @param questScheduleId 进度ID
     * @param clockInMethodIds 打卡方式ID集合
     * @return 是否添加成功
     */
    Boolean addClockInMethod2Schedule(Long questScheduleId, List<Long> clockInMethodIds);

    /**
     * 删除进度的打卡绑定
     * @param questScheduleId 进度ID
     * @return 是否删除成功
     */
    Boolean deleteByQuestScheduleId(Long questScheduleId);

    /**
     * 删除多个进度绑定的打卡
     * @param questScheduleIds 进度ID集合
     * @return 是否删除成功
     */
    Boolean deleteByQuestScheduleIds(List<Long> questScheduleIds);

    /**
     * 根据任务进度ID获取它的打卡方式ID
     * @param questScheduleId 任务进度ID
     * @return 所有打卡方式的ID
     */
    List<Long> getClockInMethodIdsByQuestScheduleId(Long questScheduleId);

    /**
     * 根据任务进度ID获取它的打卡方式
     * @param questScheduleId 进度ID
     * @return 所有打卡方式
     */
    List<ClockInMethod> getClockInMethodsByQuestScheduleId(Long questScheduleId);
}
