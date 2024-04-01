package love.ytlsnb.model.quest.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * APP任务详情页
 *
 * @author 金泓宇
 * @date 2024/3/12
 */
@Data
public class QuestInfoPageVO {
    /**
     * 主键ID
     */
    private Long questId;

    /**
     * 任务标题
     */
    private String questTitle;

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
     * 总进度数
     */
    private Long scheduleNum;

    /**
     * 奖励
     */
    private Integer reward;

    /**
     * 截止时间
     */
    private LocalDateTime endTime;

    /**
     * 任务目标
     */
    private String objective;

    /**
     * 任务描述
     */
    private String questDescription;

    /**
     * 任务提示
     */
    private String tip;

    /**
     * 所需物品
     */
    private String requiredItem;

    /**
     * 是否已完成
     */
    private Integer isFinished;
}
