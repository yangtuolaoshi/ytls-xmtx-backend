package love.ytlsnb.school.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.ytlsnb.model.school.po.Building;
import love.ytlsnb.school.mapper.BuildingMapper;
import love.ytlsnb.school.service.BuildingService;
import org.springframework.stereotype.Service;

/**
 * @author ula
 * @date 2024/2/5 9:38
 */
@Service
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements BuildingService {
}
