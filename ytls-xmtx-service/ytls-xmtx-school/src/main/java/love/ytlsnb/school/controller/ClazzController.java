package love.ytlsnb.school.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.school.dto.ClazzInsertDTO;
import love.ytlsnb.model.school.po.Clazz;
import love.ytlsnb.school.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ula
 * @date 2024/2/20 8:32
 */
@Slf4j
@RestController
@RequestMapping("/clazz")
public class ClazzController {
    @Autowired
    private ClazzService clazzService;

    @PostMapping("/clazz")
    public Result addDept(@RequestBody ClazzInsertDTO clazzInsertDTO) {
        log.info("新增班级:{}", clazzInsertDTO);
        clazzService.addClazz(clazzInsertDTO);
        return Result.ok();
    }
    @GetMapping("/list/{schoolId}")
    public Result<List<Clazz>> listClazzBySchoolId(@PathVariable Long schoolId){
        log.info("根据学校ID获取所有班级数据:{}",schoolId);
        List<Clazz> clazzList=clazzService.listClazzBySchoolId(schoolId);
        return Result.ok(clazzList);
    }
}
