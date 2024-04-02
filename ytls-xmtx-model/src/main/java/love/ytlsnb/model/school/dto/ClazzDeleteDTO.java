package love.ytlsnb.model.school.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author QiaoQiao
 * @date 2024/3/19 14:45
 */
@Data
@ToString
public class ClazzDeleteDTO {
    /**
     * 学院主键
     */
    private Long id;
    /**
     * 学院所属学校主键
     */
    private Long schoolId;
    /**
     * 学院名
     */
    private String deptName;
    /**
     * 是否删除
     */
    private Byte deleted;
}
