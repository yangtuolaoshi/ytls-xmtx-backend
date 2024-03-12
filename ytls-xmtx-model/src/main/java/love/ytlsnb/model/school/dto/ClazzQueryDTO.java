package love.ytlsnb.model.school.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ula
 * @date 2024/3/12 13:37
 */
@Data
public class ClazzQueryDTO implements Serializable {
    private Long schoolId;
    private Long deptId;
}
