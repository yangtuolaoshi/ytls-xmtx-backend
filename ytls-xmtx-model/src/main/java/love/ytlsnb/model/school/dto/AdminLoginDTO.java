package love.ytlsnb.model.school.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ula
 * @date 2024/2/6 16:25
 */
@Data
public class AdminLoginDTO {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}
