package love.ytlsnb.model.reward.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author QiaoQiao
 * @date 2024/3/22 8:11
 */
@Data
@ToString
public class ExchangeLogVO {
    /**
     * 奖品标题
     */
    private String rewardTitle;
    /**
     * 用户名
     */
    private String exUserName;
    /**
     * 学院名
     */
    private String deptName;
    /**
     * 学院名
     */
    private String clazzName;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
