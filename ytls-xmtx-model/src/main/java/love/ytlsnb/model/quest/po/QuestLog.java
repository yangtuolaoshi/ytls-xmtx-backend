package love.ytlsnb.model.quest.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务完成记录
 *
 * @author 金泓宇
 * @date 2024/3/12
 */
@Data
@TableName("tb_quest_log")
public class QuestLog {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 任务Id
     */
    @TableField("quest_id")
    private Long questId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 左值
     */
    @TableField("left_value")
    private Integer leftValue;

    /**
     * 右值
     */
    @TableField("right_value")
    private Integer rightValue;

    /**
     * 哪一棵树
     */
    @TableField("tree_id")
    private Integer treeId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
