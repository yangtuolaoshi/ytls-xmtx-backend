package love.ytlsnb.quest.service;

/**
 * 任务完成情况业务层接口
 *
 * @author 金泓宇
 * @date 2024/3/15
 */
public interface QuestLogService {
    /**
     * 根据进度检查任务是否已完成，如果已完成，就添加一条完成记录
     * @param questId 任务ID
     * @return 是否完成
     */
    Boolean isCompleted(Long questId);
}
