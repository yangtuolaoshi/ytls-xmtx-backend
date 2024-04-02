package love.ytlsnb.model.ad.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import love.ytlsnb.model.ad.po.Tag;

import java.util.List;

/**
 * @author ula
 * @date 2024/3/29 16:18
 */
@Data
public class AdvertisementVO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 广告相关所有照片
     */
    private String photos;
    /**
     * 广告的描述
     */
    private String description;
    /**
     * 广告的点击跳转链接
     */
    private String link;
    /**
     * 广告的相关标签
     */
    private List<Tag> tagList;
}
