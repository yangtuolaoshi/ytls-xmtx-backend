package love.ytlsnb.school.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.school.dto.ClazzInsertDTO;
import love.ytlsnb.school.service.ClazzService;
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
@RequestMapping("clazz")
public class ClazzController {
    @Autowired
    private ClazzService clazzService;

    @PostMapping("/clazz")
    public Result addDept(@RequestBody ClazzInsertDTO clazzInsertDTO) {
        log.info("新增班级:{}", clazzInsertDTO);
        clazzService.addClazz(clazzInsertDTO);
        return Result.ok();
    }
}
