package love.ytlsnb.school.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.ytlsnb.model.school.dto.ClazzInsertDTO;
import love.ytlsnb.model.school.dto.ClazzQueryDTO;
import love.ytlsnb.model.school.po.Clazz;

import java.util.List;

/**
 * @author ula
 * @date 2024/2/18 9:28
 */
public interface ClazzService extends IService<Clazz> {
    void addClazz(ClazzInsertDTO clazzInsertDTO);

    List<Clazz> listClazzBySchoolId(Long schoolId);

    List<Clazz> listClazzByCondition(ClazzQueryDTO clazzQueryDTO);
}
