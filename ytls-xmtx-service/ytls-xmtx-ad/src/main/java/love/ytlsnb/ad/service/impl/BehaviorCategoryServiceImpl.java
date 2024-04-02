package love.ytlsnb.ad.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.ad.mapper.BehaviorCategoryMapper;
import love.ytlsnb.ad.service.BehaviorCategoryService;
import love.ytlsnb.model.ad.po.BehaviorCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ula
 * @date 2024/3/20 15:05
 */
@Slf4j
@Service
public class BehaviorCategoryServiceImpl extends ServiceImpl<BehaviorCategoryMapper, BehaviorCategory> implements BehaviorCategoryService {
    @Autowired
    private BehaviorCategoryMapper behaviorCategoryMapper;
}
