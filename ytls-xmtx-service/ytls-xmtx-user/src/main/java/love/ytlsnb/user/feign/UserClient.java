package love.ytlsnb.user.feign;

import love.ytlsnb.model.common.Result;
import love.ytlsnb.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/user"))
public class UserClient {
    @Autowired
    private UserService userService;

    /**
     * 添加积分
     * @param reward 积分
     * @return 是否成功
     */
    @PostMapping("/addPoint")
    public Result<Boolean> addPoint(int reward) {
        return Result.ok(userService.addPoint(reward));
    }

    @PostMapping("/exchangeReward/{rewardId}")
    public Result exchangeReward(@PathVariable Long rewardId){
        userService.exchangeReward(rewardId);
        return Result.ok();
    }
}
