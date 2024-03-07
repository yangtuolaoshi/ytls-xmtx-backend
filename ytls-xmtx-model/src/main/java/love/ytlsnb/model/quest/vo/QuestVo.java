package love.ytlsnb.model.quest.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务基本信息
 *
 * @author 金泓宇
 * @date 2024/3/4
 */
@Data
public class QuestVo {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 任务标题
     */
    private String title;

    /**
     * 任务类型
     */
    private Integer type;

    /**
     * 前置任务ID
     */
    private Long parentId;

    /**
     * 前置任务标题
     */
    private String preQuestTitle;

    /**
     * 完成任务所需进度数
     */
    private Integer requiredScheduleNum;

    /**
     * 启用状态
     */
    private Integer status;

    /**
     * 奖励
     */
    private Integer reward;

    /**
     * 截止时间
     */
    private LocalDateTime endTime;
}
