package love.ytlsnb.quest.service;

/**
 * 任务进度完成记录业务层接口
 *
 * @author 金泓宇
 * @date 2024/3/15
 */
public interface QuestScheduleLogService {
    /**
     * 根据打卡记录查询任务进度是否完成，如果完成了就添加一条记录
     * @param questScheduleId 进度ID
     * @return 是否完成
     */
    Boolean isCompleted(Long questScheduleId);
}
