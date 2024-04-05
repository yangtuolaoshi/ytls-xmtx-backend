package love.ytlsnb.ad.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.ad.mapper.BehaviorCategoryMapper;
import love.ytlsnb.ad.service.BehaviorCategoryService;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.model.ad.po.BehaviorCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ula
 * @date 2024/3/20 15:05
 */
@Slf4j
@Service
public class BehaviorCategoryServiceImpl extends ServiceImpl<BehaviorCategoryMapper, BehaviorCategory> implements BehaviorCategoryService {
    @Autowired
    private BehaviorCategoryMapper behaviorCategoryMapper;

    @Override
    @Transactional
    public void addBehaviorCategory(BehaviorCategory behaviorCategory) {
        if (behaviorCategory.getType() == null
                || behaviorCategory.getScore() == null
                || behaviorCategory.getScore().compareTo(BigDecimal.ZERO) <= 0
                || StrUtil.isBlank(behaviorCategory.getDescription())) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "请校验传入参数");
        }
        List<BehaviorCategory> list = lambdaQuery().eq(BehaviorCategory::getType, behaviorCategory.getType()).list();
        if (CollectionUtil.isNotEmpty(list)) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "当前type值对应的标签已存在，请修改参数");
        }
        behaviorCategoryMapper.insert(behaviorCategory);
    }

    @Override
    public void updateBehaviorCategory(BehaviorCategory behaviorCategory) {
        if (behaviorCategory.getId() == null) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "请传入待修改对象的ID");
        }
        BehaviorCategory selectById = behaviorCategoryMapper.selectById(behaviorCategory.getId());
        if (selectById == null) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "待修改对象不存在");
        }
        if (behaviorCategory.getType() == null
                || behaviorCategory.getScore() == null
                || behaviorCategory.getScore().compareTo(BigDecimal.ZERO) <= 0
                || StrUtil.isBlank(behaviorCategory.getDescription())) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "请校验传入参数");
        }
        BehaviorCategory oldBehaviorCategory = lambdaQuery().eq(BehaviorCategory::getType, behaviorCategory.getType()).one();
        if (oldBehaviorCategory != null && !oldBehaviorCategory.getId().equals(behaviorCategory.getId())) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "当前type值对应的标签已存在，请修改参数");
        }
        behaviorCategoryMapper.updateById(behaviorCategory);
    }
}
