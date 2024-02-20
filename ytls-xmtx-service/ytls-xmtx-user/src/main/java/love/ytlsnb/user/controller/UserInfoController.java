package love.ytlsnb.user.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.user.po.UserInfo;
import love.ytlsnb.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{id}")
    public Result<UserInfo> getUserInfoById(@PathVariable Long id) {
        log.info("查询用户，id:{}", id);
        return Result.ok(userInfoService.getById(id));
    }
}
