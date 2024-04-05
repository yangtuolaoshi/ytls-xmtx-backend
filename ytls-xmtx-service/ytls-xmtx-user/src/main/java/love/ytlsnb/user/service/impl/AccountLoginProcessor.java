package love.ytlsnb.user.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.LoginType;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.model.user.dto.UserLoginDTO;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.user.service.UserLoginProcessor;
import love.ytlsnb.user.service.UserService;
import love.ytlsnb.user.utils.UserLoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ula
 * @date 2024/3/13 21:04
 */
@Slf4j
@Service
public class AccountLoginProcessor implements UserLoginProcessor {
    @Autowired
    private UserLoginUtil userLoginUtil;
    @Autowired
    private UserService userService;

    @Override
    public LoginType getType() {
        return LoginType.ACCOUNT;
    }

    @Override
    public String login(UserLoginDTO userLoginDTO) {
        if ((StrUtil.isBlankIfStr(userLoginDTO.getAccount()) || StrUtil.isBlankIfStr(userLoginDTO.getPassword()))) {
            // 账户密码为空
            throw new BusinessException(ResultCodes.BAD_REQUEST, "账户密码参数错误");
        }
        // 账户密码登录
        User user = userService.getByAccount(userLoginDTO.getAccount());
        if (user == null) {
            // 用户不存在
            throw new BusinessException(ResultCodes.UNAUTHORIZED, "用户不存在");
        }
        // 用户存在，校验密码
        if (!BCrypt.checkpw(userLoginDTO.getPassword(), user.getPassword())) {
            // 密码错误
            throw new BusinessException(ResultCodes.UNAUTHORIZED, "密码错误");
        }
        return userLoginUtil.tryLogin(user);
    }
}
