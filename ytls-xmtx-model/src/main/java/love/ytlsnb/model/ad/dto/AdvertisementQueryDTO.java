package love.ytlsnb.model.ad.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author ula
 * @date 2024/3/29 16:32
 */
@Data
@ToString
public class AdvertisementQueryDTO {
    /**
     * 带查询广告的描述信息，支持右模糊查询
     */
    String description;
    /**
     * 投放广告的客户名，支持右模糊查询
     */
    private String customerName;
    /**
     * 投放广告客户的联系方式，支持右模糊查询
     */
    private String customerPhone;
    /**
     * 当前页码
     */
    private Integer currentPage;
    /**
     * 每页大小
     */
    private Integer pageSize;
}
