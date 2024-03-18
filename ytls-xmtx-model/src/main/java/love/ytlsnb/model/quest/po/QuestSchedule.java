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
     * 管理员审核
     */
    @TableField("admin_check")
    private Integer adminCheck;

    /**
     * 照片识别
     */
    @TableField("photo_check")
    private Integer photoCheck;

    /**
     * 地点识别
     */
    @TableField("location_check")
    private Integer locationCheck;

    /**
     * 面部识别（硬件）
     */
    @TableField("face_check")
    private Integer faceCheck;

//    /**
//     * 打卡方式 三位二进制表示 XXX-上传图片/地点检测/人工审核 0为禁用 1为开启
//     */
//    @TableField("clock_in_method")
//    private Integer clockInMethod;

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
