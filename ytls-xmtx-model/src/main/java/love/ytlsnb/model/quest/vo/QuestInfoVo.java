package love.ytlsnb.model.quest.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 任务详细信息
 *
 * @author 金泓宇
 * @date 2024/3/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestInfoVo extends QuestVo {
    /**
     * 任务目标
     */
    private String objective;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 任务提示
     */
    private String tip;

    /**
     * 所需物品
     */
    private String requiredItem;
}
