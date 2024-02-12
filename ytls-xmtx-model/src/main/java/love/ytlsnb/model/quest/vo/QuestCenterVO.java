package love.ytlsnb.model.quest.vo;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

/**
 * @author ula
 * @date 2024/2/4 16:51
 */
public class QuestCenterVO {
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
     * 任务奖励
     */
    private Integer reward;
    /**
     * 任务结束时间
     */
    private LocalDateTime endTime;
    /**
     * 任务所属学校主键
     */
    private Long schoolId;
    /**
     * 任务完成状态
     */
    private Byte status;
}
