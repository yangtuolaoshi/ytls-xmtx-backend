package love.ytlsnb.school.intercepter;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import love.ytls.api.user.UserClient;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.SchoolConstant;
import love.ytlsnb.common.constants.UserConstant;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.properties.JwtProperties;
import love.ytlsnb.common.utils.ColadminHolder;
import love.ytlsnb.common.utils.JwtUtil;
import love.ytlsnb.common.utils.UserHolder;
import love.ytlsnb.model.school.po.Coladmin;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.school.service.ColadminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author ula
 */
@Component
@Slf4j
public class HolderIntercepter implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private ColadminService coladminService;
    @Lazy
    @Autowired
    private UserClient userClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("[学校服务] 正在保存调用者信息");
        String userToken = request.getHeader(jwtProperties.getUserTokenName());
        String coladminToken = request.getHeader(jwtProperties.getColadminTokenName());
        if (StrUtil.isBlankIfStr(userToken) && StrUtil.isBlankIfStr(coladminToken)) {
            // 未知调用（远程调用已修复）
            response.setStatus(ResultCodes.UNAUTHORIZED);
            return false;
        }
        if (StrUtil.isBlankIfStr(coladminToken)) {
            Claims claims = JwtUtil.parseJwt(jwtProperties.getUserSecretKey(), userToken);
            Long userId = (Long) claims.get(UserConstant.USER_ID);
            Result<User> userResult = userClient.getById(userId);
            if (userResult.getCode() != ResultCodes.OK) {
                throw new BusinessException(userResult.getCode(), userResult.getMsg());
            }
            User user = userResult.getData();
            UserHolder.saveUser(user);
        } else {
            Claims claims = JwtUtil.parseJwt(jwtProperties.getColadminSecretKey(), coladminToken);
            Long coladminId = (Long) claims.get(SchoolConstant.COLADMIN_ID);
            Coladmin coladmin = coladminService.getById(coladminId);
            ColadminHolder.saveColadmin(coladmin);
        }
        return true;
    }
}