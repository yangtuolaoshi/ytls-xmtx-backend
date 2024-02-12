package love.ytlsnb.model.reward.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ula
 * @date 2024/2/6 14:24
 */
@Data
@TableName("tb_reward")
public class Reward {
    /**
     * 奖品主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 奖品标题
     */
    private String title;
    /**
     * 奖品子标题
     */
    private String subtitle;
    /**
     * 奖品描述
     */
    private String description;
    /**
     * 奖品兑换方式
     */
    private String exchangeMethod;
    /**
     * 奖品所需积分
     */
    private Integer cost;
    /**
     * 奖品库存
     */
    private Integer stock;
    /**
     * 奖品状态
     */
    private Byte status;
    /**
     * 奖品所属学校主键
     */
    private Long schoolId;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 是否删除
     */
    @TableLogic
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private Byte deleted;
}
