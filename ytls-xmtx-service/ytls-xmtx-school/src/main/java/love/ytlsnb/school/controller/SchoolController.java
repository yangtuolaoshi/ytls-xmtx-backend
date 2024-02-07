package love.ytlsnb.school.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.school.dto.AdminLoginDTO;
import love.ytlsnb.model.school.dto.BuildingInsertDTO;
import love.ytlsnb.model.school.po.School;
import love.ytlsnb.school.service.AdminService;
import love.ytlsnb.school.service.BuildingService;
import love.ytlsnb.school.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ula
 * @date 2024/2/3 14:56
 */
@Slf4j
@RestController
@RequestMapping("school")
public class SchoolController {
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private BuildingService buildingService;
    @Autowired
    private AdminService adminService;

    @GetMapping("list")
    public Result<List<School>> list() {
        List<School> list = schoolService.list();

        return Result.ok(list);
    }

    @PostMapping("building")
    public Result addBuilding(@RequestBody BuildingInsertDTO buildingInsertDTO) {
        log.info("新增学校建筑");
        // TODO 新增学校建筑逻辑
        return Result.ok();
    }

    @PostMapping("login")
    public Result login(@RequestBody AdminLoginDTO adminLoginDTO, HttpServletRequest request) {
        log.info("管理员登录:{}", adminLoginDTO);
        adminService.login(adminLoginDTO,request);
        return Result.ok();
    }
}
