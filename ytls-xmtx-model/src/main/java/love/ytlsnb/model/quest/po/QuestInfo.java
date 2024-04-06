package love.ytlsnb.model.quest.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务详细信息模型
 *
 * @author 金泓宇
 * @date 2024/3/1
 */
@TableName("tb_quest_info")
@Data
public class QuestInfo {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 任务目标
     */
    private String objective;

    /**
     * 任务描述
     */
    @TableField("quest_description")
    private String questDescription;

    /**
     * 任务提示
     */
    private String tip;

    /**
     * 所需物品
     */
    @TableField("required_item")
    private String requiredItem;

    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd 2024-03-05")
    private LocalDateTime createTime;

    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd 2024-03-05")
    private LocalDateTime updateTime;

    @TableField("is_deleted")
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;
}
