package love.ytlsnb.model.quest.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ula
 * @date 2024/2/12 11:02
 */
@Data
@TableName("tb_quest_location")
public class QuestLocation {
    /**
     * 任务主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 对应学校数据库中的Location主键
     */
    private Long locationId;
    /**
     * 任务地点名
     */
    private String locationName;
    /**
     * 任务地点经度
     */
    private String longitude;
    /**
     * 任务地点纬度
     */
    private String latitude;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 是否删除
     */
    @TableLogic
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private Byte deleted;
}
