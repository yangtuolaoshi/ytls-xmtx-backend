package love.ytlsnb.model.quest.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 打卡记录
 *
 * @author 金泓宇
 * @date 2024/3/14
 */
@Data
@TableName("tb_clock_in_log")
public class ClockInLog {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 任务进度ID
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
}
