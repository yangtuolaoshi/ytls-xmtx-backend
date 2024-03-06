package love.ytlsnb.school.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.school.dto.DeptInsertDTO;
import love.ytlsnb.model.school.po.Dept;
import love.ytlsnb.school.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ula
 * @date 2024/2/20 8:32
 */
@Slf4j
@RestController
@RequestMapping("/dept")
public class DeptController {
    @Autowired
    private DeptService deptService;

    @PostMapping("/dept")
    public Result addDept(@RequestBody DeptInsertDTO deptInsertDTO) {
        log.info("新增学院:{}", deptInsertDTO);
        deptService.addDept(deptInsertDTO);
        return Result.ok();
    }

    @GetMapping("/list/{schoolId}")
    public Result<List<Dept>> listDeptBySchoolId(@PathVariable Long schoolId) {
        log.info("根据学校Id获取所有的学院数据:{}", schoolId);
        List<Dept> deptList = deptService.listDeptBySchoolId(schoolId);
        return Result.ok(deptList);
    }
}
