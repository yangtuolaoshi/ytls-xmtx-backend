package love.ytlsnb.model.quest.vo;

import lombok.Data;

/**
 * 进度完成情况，展示在任务详情页
 *
 * @author 金泓宇
 * @date 2024/3/12
 */
@Data
public class QuestScheduleCompletionVO {
    /**
     * 进度ID
     */
    private Long scheduleId;

    /**
     * 任务ID
     */
    private Long questId;

    /**
     * 进度标题
     */
    private String scheduleTitle;

    /**
     * 是否已完成
     */
    private Integer isFinished;
}
