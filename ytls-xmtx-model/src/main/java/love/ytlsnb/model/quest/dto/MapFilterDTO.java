package love.ytlsnb.model.quest.dto;

import lombok.Data;

/**
 * 地图过滤条件
 *
 * @author 金泓宇
 * @date 2024/3/5
 */
@Data
public class MapFilterDTO {
    /**
     * 是否获取主线任务
     */
    private Boolean getMain;

    /**
     * 是否获取支线任务
     */
    private Boolean getSide;

    /**
     * 是否获取每日任务
     */
    private Boolean getDaily;
}
