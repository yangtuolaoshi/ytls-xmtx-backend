package love.ytlsnb.model.quest.vo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ula
 * @date 2024/2/12 11:01
 */
@Data
@ToString
public class QuestVO {
    /**
     * 任务主键
     */
    private Long id;
    /**
     * 任务标题
     */
    private String title;
    /**
     * 任务目标
     */
    private String objective;
    /**
     * 任务描述
     */
    private String description;
    /**
     * 任务类型
     */
    private Byte type;
    /**
     * 任务地点主键
     */
    private Long locationId;
    /**
     * 任务地点名
     */
    private String locationName;
    /**
     * 任务地点经度
     */
    private String longitude;
    /**
     * 任务地点纬度
     */
    private String latitude;
    /**
     * 任务地点照片（cover照片排在第一）
     */
    private List<String> photos;
    /**
     * 任务所需物品
     */
    private String requiredItems;
    /**
     * 任务奖励
     */
    private Integer reward;
    /**
     * 任务时限（秒）：与任务结束时间最多存在一个属性
     */
    private Integer timeLimit;
    /**
     * 任务结束时间：与任务时限最多存在一个属性
     */
    private LocalDateTime endTime;
    /**
     * 任务所属学校主键
     */
    private Long schoolId;
    /**
     * 任务的父结点
     */
    private Long parentId;
    /**
     * 任务左值
     */
    private Long leftValue;
    /**
     * 任务右值
     */
    private Long rightValue;
}
