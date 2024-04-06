package love.ytlsnb.user.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.user.po.UserInfo;
import love.ytlsnb.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ula
 * @date 2024/2/20 9:20
 */
@Slf4j
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping
    public Result<UserInfo> getUserInfo() {
        log.info("用户端查询用户信息");
        return Result.ok(userInfoService.getUserInfo());
    }
    @GetMapping("/{id}")
    public Result<UserInfo> getUserInfoById(@PathVariable Long id) {
        log.info("查询用户，id:{}", id);
        return Result.ok(userInfoService.getById(id));
    }
}
