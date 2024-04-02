package love.ytlsnb.ad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.ytlsnb.model.ad.po.Tag;
import love.ytlsnb.model.ad.vo.TagVO;

import java.util.List;

/**
 * @author ula
 * @date 2024/3/20 14:45
 */
public interface TagService extends IService<Tag> {
    List<Tag> listAllByAdvertisementId(Long adId);

    List<Tag> listAllByUserId(Long userId);

    void addTag(Tag tag);

    void updateTag(Tag tag);

    List<TagVO> listByParentId(Long parentId);

    List<TagVO> listAll();

    TagVO getTagVOById(Long tagId);

    void deleteById(Long tagId);

    Boolean checkTagIdList(List<Long> tagIdList);
}
