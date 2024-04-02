package love.ytlsnb.quest.service;

import love.ytlsnb.model.quest.po.ClockInMethod;

import java.util.List;

/**
 * 打卡方式业务层接口
 *
 * @author 金泓宇
 * @date 2024/3/14
 */
public interface ClockInMethodService {
    /**
     * 查询所有打卡方式
     * @return 打卡方式集合
     */
    List<ClockInMethod> getAll();

    /**
     * 根据任务进度ID获取它的所有打卡方式
     * @param questScheduleId 进度Id
     * @return 这个进度的所有打卡方式
     */
    List<ClockInMethod> getByQuestScheduleId(Long questScheduleId);
}
