package love.ytlsnb.model.quest.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("tb_quest_schedule")
@Data
public class QuestSchedule {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 任务ID
     */
    @TableField("quest_id")
    private Long questId;

    /**
     * 地点ID
     */
    @TableField("location_id")
    private Long locationId;

    /**
     * 进度标题
     */
    @TableField("schedule_title")
    private String scheduleTitle;

    /**
     * 启用地点 0-禁用 1-启用
     */
    @TableField("need_location")
    private Integer needLocation;

    /**
     * 进度开启状态
     */
    private Integer scheduleStatus;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableField("is_deleted")
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;
}
