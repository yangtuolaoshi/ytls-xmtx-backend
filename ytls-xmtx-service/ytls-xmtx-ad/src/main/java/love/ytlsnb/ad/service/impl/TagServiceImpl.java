package love.ytlsnb.ad.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.ad.mapper.TagMapper;
import love.ytlsnb.ad.service.TagService;
import love.ytlsnb.common.constants.AdvertisementConstant;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.model.ad.po.Tag;
import love.ytlsnb.model.ad.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author ula
 * @date 2024/3/20 14:46
 */
@Slf4j
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<Tag> listAllByAdvertisementId(Long adId) {
        return tagMapper.listAllTagsByAdvertisementId(adId);
    }

    @Override
    public List<Tag> listAllByUserId(Long userId) {
        return tagMapper.listAllTagsByUserId(userId);
    }

    @Override
    public void addTag(Tag tag) {
        // 参数校验
        Byte level = tag.getLevel();
        if (StrUtil.isBlank(tag.getDescription()) || level == null
                || (!level.equals(AdvertisementConstant.FIRST_LEVEL) && !level.equals(AdvertisementConstant.SECOND_LEVEL))) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "请校验请求参数");
        }
        if (level == 2) {
            if (tag.getParentId() == null) {
                throw new BusinessException(ResultCodes.BAD_REQUEST, "请校验请求参数");
            }
            Tag parentTag = tagMapper.selectById(tag.getParentId());
            if (parentTag == null) {
                throw new BusinessException(ResultCodes.BAD_REQUEST, "请校验请求参数");
            }
        }
        Tag selectOne = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                .eq(Tag::getDescription, tag.getDescription()));
        if (selectOne != null) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "标签已存在");
        }

        // 插入标签
        tagMapper.insert(tag);
        if (level == 1) {
            tag.setParentId(tag.getId());
            tagMapper.updateById(tag);
        }
    }

    @Override
    public void updateTag(Tag tag) {
        // 参数校验
        Byte level = tag.getLevel();
        if (StrUtil.isBlank(tag.getDescription())) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "请校验请求参数");
        }
        Tag oldTag = lambdaQuery().eq(Tag::getId, tag.getId()).one();
        if (tag.getLevel() != null && !tag.getLevel().equals(oldTag.getLevel())) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "不能修改标签等级");
        }
        tagMapper.updateById(tag);
    }

    @Override
    public TagVO getTagVOById(Long id) {
        Tag tag = tagMapper.selectById(id);
        TagVO tagVO = BeanUtil.copyProperties(tag, TagVO.class);
        List<TagVO> subTagList = listByParentId(id);
        tagVO.setSubTagList(subTagList);
        return tagVO;
    }

    @Override
    public void deleteById(Long tagId) {
        List<TagVO> subTagList = listByParentId(tagId);
        if (!CollectionUtil.isEmpty(subTagList)) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "当前标签存在子标签，请检查后操作");
        } else {
            tagMapper.deleteById(tagId);
        }
    }

    @Override
    public Boolean checkTagIdList(List<Long> tagIdList) {
        for (Long tagId : tagIdList) {
            Tag tag = tagMapper.selectById(tagId);
            if (tag == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 根据父标签ID查询标签
     *
     * @param parentId 父标签ID
     * @return 子标签集合(不包含父结点本身)
     */
    @Override
    public List<TagVO> listByParentId(Long parentId) {
        return BeanUtil.copyToList(tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .eq(Tag::getParentId, parentId)
                .ne(Tag::getId, parentId)), TagVO.class);
    }

    @Override
    public List<TagVO> listAll() {
        // 查询所有一级标签
        List<Tag> parentsList = tagMapper.ListParents();
        // 将标签对象转换为试图对象
        List<TagVO> tagVOList = BeanUtil.copyToList(parentsList, TagVO.class);
        // 根据一级标签查询所有相关的二级标签
        for (TagVO tagVO : tagVOList) {
            tagVO.setSubTagList(listByParentId(tagVO.getId()));
        }
        // 返回数据
        return tagVOList;
    }
}
