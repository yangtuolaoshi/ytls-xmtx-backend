package love.ytlsnb.user.service;

import love.ytlsnb.model.common.LoginType;
import love.ytlsnb.model.user.dto.UserLoginDTO;

/**
 * @author ula
 * @date 2024/3/13 19:55
 */
public interface UserLoginProcessor {
    LoginType getType();

    /**
     * 封装用户进行登录的逻辑代码，登录失败则抛出异常，登陆成功则返回JWT令牌
     *
     * @param userLoginDTO 封装用户登录的相关参数
     * @return 登陆成功的JWT令牌
     */
    String login(UserLoginDTO userLoginDTO);
}
