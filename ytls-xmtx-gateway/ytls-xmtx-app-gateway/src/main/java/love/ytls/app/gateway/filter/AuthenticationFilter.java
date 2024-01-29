package love.ytls.app.gateway.filter;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import love.ytls.api.user.UserClient;
import love.ytlsnb.common.constants.UserConstant;
import love.ytlsnb.common.properties.JwtProperties;
import love.ytlsnb.common.utils.JwtUtil;
import love.ytlsnb.common.utils.UserHolder;
import love.ytlsnb.model.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author ula
 */
@Order(0)
@Component
public class AuthenticationFilter implements GlobalFilter {
    @Autowired
    private JwtProperties jwtProperties;
    /**
     * 因为存在循环依赖问题，这里采用Lazy加载模式
     */
    @Autowired
    @Lazy
    private UserClient userClient;

    /**
     * 鉴权过滤器 验证token
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1. 获取请求
        ServerHttpRequest request = exchange.getRequest();
        //2. 获取响应
        ServerHttpResponse response = exchange.getResponse();
        //3. 如果是登录/注册请求则放行
        String requestPath = request.getURI().getPath();
        if (requestPath.contains(UserConstant.LOGIN_URL) || requestPath.contains(UserConstant.REGISTER_URL)) {
            return chain.filter(exchange);
        }
        //4. 获取请求头
        HttpHeaders headers = request.getHeaders();
        //5. 请求头中获取令牌
        String token = headers.getFirst(jwtProperties.getUserTokenName());
        //6. 判断请求头中是否有令牌
        if (StrUtil.isBlank(token)) {
            //7. 响应中放入返回的状态吗, 没有权限访问
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //8. 返回
            return response.setComplete();
        }

        //9. 如果请求头中有令牌则解析令牌
        try {
            // 解析参数
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);

            // 查询用户数据
            Long userId = Long.valueOf(claims.get(UserConstant.USER_ID).toString());
            User user = userClient.getById(userId).getData();
            // 字段脱敏
            user.setPassword("");
            UserHolder.saveUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            //10. 解析jwt令牌出错, 说明令牌过期或者伪造等不合法情况出现
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //11. 返回
            return response.setComplete();
        }
        //12. 放行
        return chain.filter(exchange);

    }
}
