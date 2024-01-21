package love.ytlsnb.model.user.pojo;

import lombok.Data;

import java.time.LocalDateTime;

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
