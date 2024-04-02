package love.ytlsnb.model.quest.doc;

import lombok.Data;

/**
 * 索引库数据封装
 *
 * @author 金泓宇
 * @date 2024/3/31
 */
@Data
public class QuestScheduleDoc {
    /**
     * 学校ID
     */
    private Long schoolId;

    /**
     * 任务标题
     */
    private String questTitle;

    /**
     * 任务ID
     */
    private Long questId;

    /**
     * 任务类型
     */
    private Integer type;

    /**
     * 任务目标
     */
    private String objective;

    /**
     * 任务详情
     */
    private String questDescription;

    /**
     * 所需物品
     */
    private String requiredItem;

    /**
     * 任务提示
     */
    private String tip;

    /**
     * 任务奖励
     */
    private Integer reward;

    /**
     * 进度ID
     */
    private Long questScheduleId;

    /**
     * 进度标题
     */
    private String questScheduleTitle;

    /**
     * 启用地点 0-禁用 1-启用
     */
    private Integer needLocation;

    /**
     * 任务地点ID
     */
    private Long questLocationId;

    /**
     * 地点名称
     */
    private String locationName;

    /**
     * 地点描述
     */
    private String locationDescription;
}
