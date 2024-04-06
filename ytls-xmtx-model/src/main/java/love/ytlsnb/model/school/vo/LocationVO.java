package love.ytlsnb.model.school.vo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ula
 * @date 2024/2/12 15:21
 */
@Data
@ToString
public class LocationVO {
    /**
     * 学校建筑主键
     */
    private Long id;
    /**
     * 关联的学校主键
     */
    private Long schoolId;
    /**
     * 建筑名
     */
    private String locationName;
    /**
     * 学校建筑的经度
     */
    private String longitude;
    /**
     * 学校建筑的维度
     */
    private String latitude;

    /**
     * 相关照片地址
     */
    private List<String> photos;
}
