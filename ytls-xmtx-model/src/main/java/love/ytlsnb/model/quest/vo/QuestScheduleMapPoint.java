package love.ytlsnb.model.quest.vo;

import lombok.Data;

/**
 * 进度地图坐标点
 *
 * @author 金泓宇
 * @date 2024/3/12
 */
@Data
public class QuestScheduleMapPoint {
    /**
     * 进度ID
     */
    private Long scheduleId;

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
