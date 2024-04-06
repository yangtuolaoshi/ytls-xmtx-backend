package love.ytlsnb.model.quest.vo;

import lombok.Data;

/**
 * 任务信息，用于列表展示
 *
 * @author 金泓宇
 * @date 2024/3/13
 */
@Data
public class QuestListItemVO {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 任务类型
     */
    private Integer type;

    /**
     * 任务标题
     */
    private String questTitle;

    /**
     * 任务奖励
     */
    private Integer reward;
}
