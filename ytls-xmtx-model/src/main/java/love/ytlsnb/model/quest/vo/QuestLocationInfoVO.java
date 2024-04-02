package love.ytlsnb.model.quest.vo;

import lombok.Data;

import java.util.List;

/**
 * 任务地点详细信息，用于进度详情页
 *
 * @author 金泓宇
 * @date 2024/3/12
 */
@Data
public class QuestLocationInfoVO {
    /**
     * 地点ID
     */
    private Long locationId;

    /**
     * 地点名称
     */
    private String locationName;

    /**
     * 地点描述
     */
    private String locationDescription;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 地点照片
     */
    private List<String> photoUrls;
}
