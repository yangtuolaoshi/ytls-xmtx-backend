package love.ytlsnb.school.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.school.dto.DeptInsertDTO;
import love.ytlsnb.school.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ula
 * @date 2024/2/20 8:32
 */
@Slf4j
@RestController
@RequestMapping("dept")
public class DeptController {
    @Autowired
    private DeptService deptService;

    @PostMapping("/dept")
    public Result addDept(@RequestBody DeptInsertDTO deptInsertDTO) {
        log.info("新增学院:{}", deptInsertDTO);
        deptService.addDept(deptInsertDTO);
        return Result.ok();
    }
}
