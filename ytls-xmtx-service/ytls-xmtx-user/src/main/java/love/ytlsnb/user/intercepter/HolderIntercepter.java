package love.ytlsnb.user.intercepter;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import love.ytls.api.school.SchoolClient;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.SchoolConstant;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.properties.JwtProperties;
import love.ytlsnb.common.utils.ColadminHolder;
import love.ytlsnb.common.utils.JwtUtil;
import love.ytlsnb.common.utils.UserHolder;
import love.ytlsnb.model.school.po.Coladmin;
import love.ytlsnb.model.common.Result;
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
public class HolderIntercepter implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private UserService userService;
    @Lazy
    @Autowired
    private SchoolClient schoolClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("[用户服务] 正在保存调用者信息");
        String userToken = request.getHeader(jwtProperties.getUserTokenName());
        String coladminToken = request.getHeader(jwtProperties.getColadminTokenName());
        if (StrUtil.isBlankIfStr(userToken) && StrUtil.isBlankIfStr(coladminToken)) {
            // 远程调用，直接放行
            return true;
        }
        if (StrUtil.isBlankIfStr(userToken)) {
            // 学校管理员调用，保存学校管理员信息
            Claims claims = JwtUtil.parseJwt(jwtProperties.getColadminSecretKey(), coladminToken);
            Long coladminId = (Long) claims.get(SchoolConstant.COLADMIN_ID);
            Result<Coladmin> coladminResult = schoolClient.getColadminById(coladminId);
            if (coladminResult.getCode() != ResultCodes.OK) {
                throw new BusinessException(coladminResult.getCode(), coladminResult.getMsg());
            }
            Coladmin coladmin = coladminResult.getData();
            ColadminHolder.saveColadmin(coladmin);
        } else {
            // 用户管理员调用，保存用户管理员信息
            Claims claims = JwtUtil.parseJwt(jwtProperties.getUserSecretKey(), userToken);
            Long userId = (Long) claims.get(SchoolConstant.USER_ID);
            User user = userService.getById(userId);
            UserHolder.saveUser(user);
        }
        return true;
    }
}