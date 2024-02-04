package love.ytlsnb.school.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.ytlsnb.model.school.po.School;
import love.ytlsnb.school.mapper.SchoolMapper;
import love.ytlsnb.school.service.SchoolService;
import org.springframework.stereotype.Service;

/**
 * @author ula
 * @date 2024/2/3 14:59
 */
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements SchoolService {
}
