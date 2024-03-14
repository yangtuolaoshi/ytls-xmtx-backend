package love.ytlsnb.school.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.coladmin.dto.ColadminLoginDTO;
import love.ytlsnb.model.coladmin.dto.ColadminRegisterDTO;
import love.ytlsnb.model.coladmin.po.Coladmin;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.school.service.ColadminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ula
 * @date 2024/2/28 20:07
 */
@Slf4j
@RestController
@RequestMapping("/coladmin")
public class ColadminController {
    @Autowired
    private ColadminService coladminService;

    @PostMapping("/login")
    public Result<String> login(@RequestBody ColadminLoginDTO coladminLoginDTO, HttpServletRequest request) {
        log.info("管理员登录:{}", coladminLoginDTO);
        String jwt = coladminService.login(coladminLoginDTO, request);
        return Result.ok(jwt);
    }

    @PostMapping("/register")
    public Result register(@RequestBody ColadminRegisterDTO coladminRegisterDTO) {
        log.info("管理员注册:{}", coladminRegisterDTO);
        coladminService.register(coladminRegisterDTO);
        return Result.ok();
    }

    @GetMapping("/insensitiveColadmin/{id}")
    public Result<Coladmin> getInsensitiveColadminById(@PathVariable Long id) {
        log.info("获取不敏感的学校管理员相关信息");
        Coladmin coladmin = coladminService.selectInsensitiveAdminById(id);
        return Result.ok(coladmin);
    }



}
