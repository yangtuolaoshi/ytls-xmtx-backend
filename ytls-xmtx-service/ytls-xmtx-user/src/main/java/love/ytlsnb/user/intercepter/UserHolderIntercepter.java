package love.ytlsnb.user.intercepter;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.UserConstant;
import love.ytlsnb.common.properties.JwtProperties;
import love.ytlsnb.common.utils.JwtUtil;
import love.ytlsnb.common.utils.UserHolder;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author ula
 */
@Component
@Slf4j
public class UserHolderIntercepter implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("正在保存用户信息");
        String token = request.getHeader(jwtProperties.getUserTokenName());
        // token为空，为远程调用，直接放行
        if(StrUtil.isBlankIfStr(token)){
            return true;
        }
        Claims claims = JwtUtil.parseJwt(jwtProperties.getUserSecretKey(), token);
        Long userId = (Long) claims.get(UserConstant.USER_ID);
        User user=userService.getById(userId);
        UserHolder.saveUser(user);
        return true;
    }
}
