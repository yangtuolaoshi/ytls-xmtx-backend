package love.ytlsnb.model.school.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author ula
 * @date 2024/2/18 10:03
 */
@Data
@ToString
public class ClazzInsertDTO {
    private Long deptId;
    private String clazzName;
}
