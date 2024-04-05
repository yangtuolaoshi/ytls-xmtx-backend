package love.ytlsnb.model.quest.vo;

import lombok.Data;

/**
 * 任务小卡片
 *
 * @author 金泓宇
 * @date 2024/3/31
 */
@Data
public class QuestCardVO {
    /**
     * 任务ID
     */
    private Long questId;

    /**
     * 进度ID
     */
    private Long questScheduleId;

    /**
     * 任务标题
     */
    private String questTitle;

    /**
     * 任务描述
     */
    private String questDescription;

    /**
     * 任务类型
     */
    private Integer type;

    /**
     * 进度标题
     */
    private String scheduleTitle;

    /**
     * 任务奖励
     */
    private Integer reward;

    /**
     * 距离
     */
    private Double distance;
}
