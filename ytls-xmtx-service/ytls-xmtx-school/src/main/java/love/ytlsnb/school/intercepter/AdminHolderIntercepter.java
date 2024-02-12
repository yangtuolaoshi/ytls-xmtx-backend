package love.ytlsnb.school.intercepter;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.SchoolConstant;
import love.ytlsnb.common.constants.UserConstant;
import love.ytlsnb.common.properties.JwtProperties;
import love.ytlsnb.common.utils.AdminHolder;
import love.ytlsnb.common.utils.JwtUtil;
import love.ytlsnb.common.utils.UserHolder;
import love.ytlsnb.model.school.po.Admin;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.school.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author ula
 */
@Component
@Slf4j
public class AdminHolderIntercepter implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private AdminService adminService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("正在保存用户信息");
        String token = request.getHeader(jwtProperties.getAdminTokenName());
        // token为空，无权限（正常情况下不可能）
        if (StrUtil.isBlankIfStr(token)) {
            response.setStatus(ResultCodes.UNAUTHORIZED);
            return false;
        }
        Claims claims = JwtUtil.parseJwt(jwtProperties.getAdminSecretKey(), token);
        Long adminId = (Long) claims.get(SchoolConstant.ADMIN_ID);
        Admin admin = adminService.selectInsensitiveAdminById(adminId);
        AdminHolder.saveAdmin(admin);
        return true;
    }
}
