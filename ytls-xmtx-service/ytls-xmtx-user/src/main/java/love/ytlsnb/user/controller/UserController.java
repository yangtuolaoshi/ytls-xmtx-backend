package love.ytlsnb.user.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.user.pojo.User;
import love.ytlsnb.model.user.pojo.dto.UserLoginDTO;
import love.ytlsnb.model.user.pojo.dto.UserRegisterDTO;
import love.ytlsnb.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

/**
 * 用户基本信息控制器层
 *
 * @author 金泓宇
 * @date 2024/01/21
 */
@RestController
@RequestMapping("/user")
@RefreshScope// 必须加，从配置中心中获取配置
@Slf4j
public class UserController {
//    @Value("${pattern.format}")
//    private String format;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        log.info("查询用户，id:{}", id);
        return Result.ok(userService.selectById(id));
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("用户注册：{}", userRegisterDTO);
        userService.register(userRegisterDTO);
        return Result.ok();
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        log.info("用户登录：{}", userLoginDTO);
        String jwt = userService.login(userLoginDTO, request);
        return Result.ok(jwt);
    }
}
