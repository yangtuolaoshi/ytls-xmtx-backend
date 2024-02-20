package love.ytlsnb.school.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.school.dto.AdminLoginDTO;
import love.ytlsnb.model.school.dto.AdminRegisterDTO;
import love.ytlsnb.model.user.dto.UserInsertDTO;
import love.ytlsnb.school.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ula
 * @date 2024/2/20 8:29
 */
@Slf4j
@RestController
@RequestMapping("admin")
public class AdminController {
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
    @PostMapping("user")
    public Result addUser(@RequestBody UserInsertDTO userInsertDTO) throws Exception {
        log.info("新增用户数据:{}", userInsertDTO);
        adminService.addUser(userInsertDTO);
        return Result.ok();
    }

    @PostMapping("user/batch")
    public Result addUserBatch(MultipartFile multipartFile) throws Exception {
        log.info("批量新增用户数据");
        adminService.addUserBatch(multipartFile);
        return Result.ok();
    }
}
