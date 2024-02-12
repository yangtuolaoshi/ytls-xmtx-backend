package love.ytlsnb.model.common;

import lombok.Data;

/**
 * 用于处理缓存击穿的逻辑过期对象封装
 *
 * @author ula
 * @date 2024/1/31 9:26
 */
@Data
public class RedisData {
    /**
     * 逻辑过期时间（毫秒）
     */
    private Long expirationTime;
    /**
     * 具体缓存的对象
     */
    private Object data;
}
