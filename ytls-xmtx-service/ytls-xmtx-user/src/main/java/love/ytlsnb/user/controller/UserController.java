package love.ytlsnb.user.controller;

import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.user.pojo.User;
import love.ytlsnb.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户基本信息控制器层
 *
 * @author 金泓宇
 * @date 2024/01/21
 */
@RestController
@RequestMapping("/user")
@RefreshScope// 必须加，从配置中心中获取配置
public class UserController {
//    @Value("${pattern.format}")
//    private String format;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable String id) {
//        System.out.println(format);// 输出日期格式
        return Result.ok(userService.getById(id));
    }
}
