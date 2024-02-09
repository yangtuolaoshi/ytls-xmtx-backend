package love.ytlsnb.model.quest.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author ula
 * @date 2024/1/30 21:01
 */
@Data
@ToString
@TableName("tb_quest")
public class Quest {
    /**
     * 任务主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 任务标题
     */
    private String title;
    /**
     * 任务目标
     */
    private String objective;
    /**
     * 任务描述
     */
    private String description;
    /**
     * 任务类型
     */
    private Byte type;
    /**
     * 任务地点主键
     */
    private Long locationId;
    /**
     * 任务地点名
     */
    private String locationName;
    /**
     * 任务所需物品
     */
    private String requiredItems;
    /**
     * 任务奖励
     */
    private Integer reward;
    /**
     * 任务状态
     */
    private Byte status;
    /**
     * 任务时限（秒）：与任务结束时间最多存在一个属性
     */
    private Integer timeLimit;
    /**
     * 任务结束时间：与任务时限最多存在一个属性
     */
    private LocalDateTime endTime;
    /**
     * 任务所属学校主键
     */
    private Long schoolId;
    /**
     * 任务的父结点
     */
    private Long parentId;
    /**
     * 任务左值
     */
    private Long leftValue;
    /**
     * 任务右值
     */
    private Long rightValue;
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
