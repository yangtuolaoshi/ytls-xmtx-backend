package love.ytlsnb.app.gateway.filter;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import love.ytls.api.user.UserClient;
import love.ytlsnb.common.constants.RedisConstant;
import love.ytlsnb.common.constants.UserConstant;
import love.ytlsnb.common.properties.JwtProperties;
import love.ytlsnb.common.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author ula
 */
@Order(0)
@Component
public class AuthenticationFilter implements GlobalFilter {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 鉴权过滤器 验证token
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求
        ServerHttpRequest request = exchange.getRequest();
        // 获取响应
        ServerHttpResponse response = exchange.getResponse();
        // 如果是登录/注册请求则放行
        String requestPath = request.getURI().getPath();
        if (requestPath.contains(UserConstant.LOGIN_URL) || requestPath.contains(UserConstant.REGISTER_URL)) {
            return chain.filter(exchange);
        }
        // 获取请求头
        HttpHeaders headers = request.getHeaders();
        // 请求头中获取令牌
        String token = headers.getFirst(jwtProperties.getUserTokenName());
        // 判断请求头中是否有令牌
        if (StrUtil.isBlank(token)) {
            // 响应中放入返回的状态吗, 没有权限访问
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            // 返回
            return response.setComplete();
        }

        // 如果请求头中有令牌则解析令牌
        try {
            // 解析令牌，校验是否是合法的令牌，是否过期
            Claims claims = JwtUtil.parseJwt(jwtProperties.getUserSecretKey(), token);

            // 校验当前令牌是否有效（账户是否被其他人登录，生成了新的jwt）
            // 请求头中获取用户id
            Long userId = claims.get(UserConstant.USER_ID, Long.class);
            String userLoginKey = RedisConstant.USER_LOGIN_PREFIX + userId;
            // redis中的签名
            String redisSignature = redisTemplate.opsForValue().get(userLoginKey);
            // 当前请求中的签名
            String userSignature = token.substring(token.lastIndexOf('.') + 1);
            if (redisSignature != null && !userSignature.equals(redisSignature)) {
                // 有其他用户登录账户，当前jwt令牌已失效
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                // 返回
                return response.setComplete();
            }
            return chain.filter(exchange);
        } catch (Exception e) {
            e.printStackTrace();
            // 解析jwt令牌出错, 说明令牌过期或者伪造等不合法情况出现
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            // 返回
            return response.setComplete();
        }
    }
}
