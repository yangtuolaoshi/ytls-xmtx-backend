package love.ytlsnb.model.quest.vo;

import lombok.Data;

/**
 * 进度简要信息展示，主要用于地图展示
 *
 * @author 金泓宇
 * @date 2024/3/5
 */
@Data
public class QuestScheduleVo {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 任务类型
     */
    private Integer type;

    /**
     * 任务标题
     */
    private String questTitle;

    /**
     * 进度标题
     */
    private String scheduleTitle;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;
}
