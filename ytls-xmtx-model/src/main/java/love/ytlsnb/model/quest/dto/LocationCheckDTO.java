package love.ytlsnb.model.quest.dto;

import lombok.Data;

/**
 * 地点打卡条件
 *
 * @author 金泓宇
 * @date 2024/3/14
 */
@Data
public class LocationCheckDTO {
    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;
}
