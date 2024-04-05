package love.ytlsnb.model.reward.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author QiaoQiao
 * @date 2024/3/22 8:36
 */
@Data
@ToString
public class ExchangeLogQueryDTO {
    /**
     * 奖品兑换主键
     */
    private Long id;
    /**
     * 奖品主键
     */
    private Long rewardId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 学校id
     */
    private Long schoolId;
    /**
     * 奖品名称
     */
    private String rewardTitle;
    /**
     * 奖品状态
     */
    private Byte status;
    /**
     * 学生名称
     */
    private String studentName;
    /**
     * 学院名称
     */
    private String deptName;
    /**
     * 班级名称
     */
    private String clazzName;
    /**
     * 奖品开始兑换时间
     */
    private LocalDateTime startTime;
    /**
     * 奖品结束兑换时间
     */
    private LocalDateTime endTime;
}
