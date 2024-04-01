package love.ytlsnb.model.quest.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务进度-打卡方式关联表
 *
 * @author 金泓宇
 * @date 2024/3/14
 */
@Data
@TableName("tb_quest_schedule_clock_in_method")
public class QuestScheduleClockInMethod {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 进度ID
     */
    @TableField("quest_schedule_id")
    private Long questScheduleId;

    /**
     * 打卡方式ID
     */
    @TableField("clock_in_method_id")
    private Long clockInMethodId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 是否删除
     */
    @TableField("is_deleted")
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;
}
