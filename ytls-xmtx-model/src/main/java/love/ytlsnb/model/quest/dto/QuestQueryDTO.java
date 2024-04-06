package love.ytlsnb.model.quest.dto;

import lombok.Data;

/**
 * 任务查询条件
 *
 * @author 金泓宇
 * @date 2024/3/1
 */
@Data
public class QuestQueryDTO {
    /**
     * 任务标题
     */
    private String title;

    /**
     * 任务类型
     */
    private Integer type;

    /**
     * 学校ID
     */
    private Long schoolId;
}
