package love.ytlsnb.model.user.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户类
 *
 * @author 金泓宇
 * @date 2024/1/21
 */
@Data
public class User {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 用户名
     */
    private String name;
}
