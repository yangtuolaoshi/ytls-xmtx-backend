package love.ytlsnb.user.service.impl;

import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.LoginType;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.utils.CommonUtil;
import love.ytlsnb.model.user.dto.UserLoginDTO;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.user.mapper.UserMapper;
import love.ytlsnb.user.service.UserLoginProcessor;
import love.ytlsnb.user.utils.UserLoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ula
 * @date 2024/3/13 20:06
 */
@Slf4j
@Service
public class PhoneLoginProcessor implements UserLoginProcessor {
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private UserLoginUtil userLoginUtil;
    @Autowired
    private UserMapper userMapper;

    @Override
    public LoginType getType() {
        return LoginType.PHONE;
    }

    @Override
    public String login(UserLoginDTO userLoginDTO) {
        if ((StrUtil.isBlankIfStr(userLoginDTO.getPhone()) || StrUtil.isBlankIfStr(userLoginDTO.getCode()))) {
            // 手机号验证码为空
            throw new BusinessException(ResultCodes.BAD_REQUEST, "登录请求参数错误");
        }
        //手机号验证码登录
        if (!PhoneUtil.isPhone(userLoginDTO.getPhone())) {
            throw new BusinessException(ResultCodes.BAD_REQUEST, "请填写正确的手机号");
        }
        if (!commonUtil.checkShortMessage(userLoginDTO.getPhone(), userLoginDTO.getCode())) {
            // 验证码不同
            throw new BusinessException(ResultCodes.UNAUTHORIZED, "验证码错误");
        }
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, userLoginDTO.getPhone()));
        if (user == null) {
            // 查询用户数据为空
            throw new BusinessException(ResultCodes.UNAUTHORIZED, "当前手机号尚未注册账号");
        }
        return userLoginUtil.tryLogin(user);
    }
}
