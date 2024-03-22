package love.ytlsnb.school.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.school.dto.DeptInsertDTO;
import love.ytlsnb.model.school.po.Dept;
import love.ytlsnb.model.school.vo.DeptVO;

import java.util.List;

/**
 * @author ula
 * @date 2024/2/18 9:28
 */
public interface DeptService extends IService<Dept> {
    void addDept(DeptInsertDTO deptInsertDTO);

    List<Dept> listDeptBySchoolId(Long schoolId);

    Boolean update(DeptInsertDTO deptInsertDTO);

    Boolean deleteDept(DeptInsertDTO deptInsertDTO);

    PageResult<List<DeptVO>> getPageByCondition(DeptInsertDTO deptQueryDTO, int page, int size);
}
