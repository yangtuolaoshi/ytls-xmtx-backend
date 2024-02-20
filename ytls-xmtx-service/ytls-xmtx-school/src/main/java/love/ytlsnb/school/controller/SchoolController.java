package love.ytlsnb.school.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.school.dto.*;
import love.ytlsnb.model.school.po.Location;
import love.ytlsnb.model.school.po.School;
import love.ytlsnb.model.school.vo.LocationVO;
import love.ytlsnb.model.user.dto.UserInsertDTO;
import love.ytlsnb.school.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private DeptService deptService;
    @Autowired
    private ClazzService clazzService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private AdminService adminService;

    @PostMapping("login")
    public Result<String> login(@RequestBody AdminLoginDTO adminLoginDTO, HttpServletRequest request) {
        log.info("管理员登录:{}", adminLoginDTO);
        String jwt = adminService.login(adminLoginDTO, request);
        return Result.ok(jwt);
    }

    @PostMapping("register")
    public Result register(@RequestBody AdminRegisterDTO adminRegisterDTO) {
        log.info("管理员注册:{}", adminRegisterDTO);
        adminService.register(adminRegisterDTO);
        return Result.ok();
    }

    @GetMapping("list")
    public Result<List<School>> list() {
        List<School> list = schoolService.list();
        return Result.ok(list);
    }

    @PostMapping("/dept")
    public Result addDept(@RequestBody DeptInsertDTO deptInsertDTO) {
        log.info("新增学院:{}", deptInsertDTO);
        deptService.addDept(deptInsertDTO);
        return Result.ok();
    }
    @PostMapping("/clazz")
    public Result addDept(@RequestBody ClazzInsertDTO clazzInsertDTO) {
        log.info("新增班级:{}", clazzInsertDTO);
        clazzService.addClazz(clazzInsertDTO);
        return Result.ok();
    }

    @PostMapping("location")
    public Result addLocation(@RequestBody LocationInsertDTO locationInsertDTO) {
        log.info("新增学校建筑");
        // TODO 新增学校建筑逻辑
        return Result.ok();
    }

    @GetMapping("location/{locationId}")
    public Result<LocationVO> getWholeLocationById(@PathVariable Long locationId) {
        log.info("查询学校地点:{}", locationId);
        return Result.ok(locationService.getWholeLocationById(locationId));
    }

    @PostMapping("user")
    public Result addUser(@RequestBody UserInsertDTO userInsertDTO) throws Exception {
        log.info("新增用户数据:{}", userInsertDTO);
        schoolService.addUser(userInsertDTO);
        return Result.ok();
    }

    @PostMapping("user/batch")
    public Result addUserBatch(MultipartFile multipartFile) throws Exception {
        log.info("批量新增用户数据");
        schoolService.addUserBatch(multipartFile);
        return Result.ok();
    }
}