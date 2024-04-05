package love.ytlsnb.model.ad.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import love.ytlsnb.model.ad.po.Tag;

import java.util.List;

/**
 * @author ula
 * @date 2024/3/28 16:55
 */
@Data
public class TagVO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 标签等级
     */
    private Byte level;
    /**
     * 父标签ID
     */
    private Long parentId;
    /**
     * 当前标签的描述
     */
    private String description;
    /**
     * 子标签集合
     */
    private List<TagVO> subTagList;
}
