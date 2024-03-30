package love.ytlsnb.model.quest.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务实体类
 *
 * @author 金泓宇
 * @date 2024/2/29
 */
@TableName("tb_quest")
@Data
public class Quest {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 任务类型：0-主线 1-支线 2-每日 3-活动
     */
    private Integer type;

    /**
     * 任务标题
     */
    @TableField("quest_title")
    private String questTitle;

    /**
     * 任务详情ID
     */
    private Long infoId;

    /**
     * 积分奖励
     */
    private Integer reward;

    /**
     * 任务完成所需进度数
     */
    @TableField("required_schedule_num")
    private Integer requiredScheduleNum;

    /**
     * 启用状态
     */
    @TableField("quest_status")
    private Integer questStatus;

    /**
     * 截止时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 学校ID
     */
    @TableField("school_id")
    private Long schoolId;

    /**
     * 父结点ID
     */
    @TableField("parent_id")
    private Long parentId;

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
     * 树ID-指第几棵树
     */
    @TableField("tree_id")
    private Integer treeId;

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

    // 重写哈希值和equals，方便使用哈希表
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Quest quest = (Quest) o;
        return id != null ? id.equals(quest.id) : quest.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
