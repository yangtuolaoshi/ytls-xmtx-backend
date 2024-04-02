package love.ytlsnb.quest.intercepter;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import love.ytls.api.school.SchoolClient;
import love.ytls.api.user.UserClient;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.SchoolConstant;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.properties.JwtProperties;
import love.ytlsnb.common.utils.ColadminHolder;
import love.ytlsnb.common.utils.JwtUtil;
import love.ytlsnb.common.utils.UserHolder;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.school.po.Coladmin;
import love.ytlsnb.model.user.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 任务服务过滤器
 */
@Component
@Slf4j
public class QuestInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;

    @Lazy
    @Autowired
    private UserClient userClient;

    @Lazy
    @Autowired
    private SchoolClient schoolClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("正在保存用户信息");
        // TODO jwtProperties的coladmin信息无法正常注入，现在只能写死
        String userToken = request.getHeader(jwtProperties.getUserTokenName());
//        String coladminToken = request.getHeader(jwtProperties.getColadminTokenName());
        String coladminToken = request.getHeader("Authentication-Coladmin");
        if (!StrUtil.isBlankIfStr(coladminToken)) {
            // 管理员
//            Claims claims = JwtUtil.parseJwt(jwtProperties.getColadminSecretKey(), coladminToken);
            Claims claims = JwtUtil.parseJwt("abcdefghabcdefghabcdefghabcdefgh", coladminToken);
            Long coladminId = (Long) claims.get(SchoolConstant.COLADMIN_ID);
            Result<Coladmin> coladminResult = schoolClient.getColadminById(coladminId);
            if (coladminResult.getCode() != ResultCodes.OK) {
                throw new BusinessException(coladminResult.getCode(), coladminResult.getMsg());
            }
            Coladmin coladmin = coladminResult.getData();
            ColadminHolder.saveColadmin(coladmin);
        } else if (!StrUtil.isBlankIfStr(userToken)) {
            // 用户
            Claims claims = JwtUtil.parseJwt(jwtProperties.getUserSecretKey(), userToken);
            Long userId = (Long) claims.get(SchoolConstant.USER_ID);
            User user = userClient.getUserById(userId).getData();
            UserHolder.saveUser(user);
        } else {
            // 都为空说明未登录，不能放行
            response.setStatus(ResultCodes.UNAUTHORIZED);
            return false;
        }
        return true;
    }
}
