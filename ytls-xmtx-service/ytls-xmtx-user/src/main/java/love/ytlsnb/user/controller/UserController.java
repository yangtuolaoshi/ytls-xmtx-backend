package love.ytlsnb.user.controller;

import love.ytlsnb.model.user.pojo.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RefreshScope// 必须加，从配置中心中获取配置
public class UserController {
    @Value("${pattern.format}")
    private String format;

    @GetMapping("/get")
    public User getById() {
        User user = new User();
        user.setId("1");
        user.setName("zhangsan");
        System.out.println(format);// 输出日期格式
        return user;
    }
}
