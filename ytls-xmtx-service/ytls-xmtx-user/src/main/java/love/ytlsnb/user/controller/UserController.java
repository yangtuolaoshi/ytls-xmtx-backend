package love.ytlsnb.user.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.user.pojo.User;
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
        log.info("查询用户，id:{}",id);
        return Result.ok(userService.getById(id));
    }
    @PostMapping("/register")
    public Result<String> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("用户注册：{}",userRegisterDTO);
        String jwt=userService.register(userRegisterDTO);
        return Result.ok(jwt);
    }
}
