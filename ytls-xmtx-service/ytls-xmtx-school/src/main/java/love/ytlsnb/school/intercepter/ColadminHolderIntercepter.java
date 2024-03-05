package love.ytlsnb.school.intercepter;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.SchoolConstant;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.properties.JwtProperties;
import love.ytlsnb.common.utils.ColadminHolder;
import love.ytlsnb.common.utils.JwtUtil;
import love.ytlsnb.model.coladmin.po.Coladmin;
import love.ytlsnb.model.common.Result;
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
public class ColadminHolderIntercepter implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private ColadminService coladminService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("正在保存学校管理员信息");
        String token = request.getHeader(jwtProperties.getColadminTokenName());
        // token为空，无权限（正常情况下不可能???）
        if (StrUtil.isBlankIfStr(token)) {
            response.setStatus(ResultCodes.UNAUTHORIZED);
            return false;
        }
        Claims claims = JwtUtil.parseJwt(jwtProperties.getColadminSecretKey(), token);
        Long coladminId = (Long) claims.get(SchoolConstant.COLADMIN_ID);
        Coladmin coladmin = coladminService.getById(coladminId);
        ColadminHolder.saveColadmin(coladmin);
        return true;
    }
}
