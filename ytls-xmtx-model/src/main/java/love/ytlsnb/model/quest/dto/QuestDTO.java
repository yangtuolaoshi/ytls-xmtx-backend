package love.ytlsnb.model.quest.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务或进度添加修改表单数据
 *
 * @author 金泓宇
 * @date 2024/3/1
 */
@Data
public class QuestDTO {
    /**
     * 学校ID
     */
    private Long schoolId;

    /**
     * 任务标题
     */
    private String questTitle;

    /**
     * 前置任务ID
     */
    private Long preQuestId;

    /**
     * 任务类型
     */
    private Integer type;

    /**
     * 任务目标
     */
    private String objective;

    /**
     * 任务详情
     */
    private String questDescription;

    /**
     * 所需物品
     */
    private String requiredItem;

    /**
     * 任务提示
     */
    private String tip;

    /**
     * 任务奖励
     */
    private Integer reward;

    /**
     * 任务完成所需进度数
     */
    private Integer requiredScheduleNum;

    /**
     * 任务启用状态
     */
    private Integer questStatus;

    /**
     * 截止时间
     */
    private LocalDateTime endTime;

    /*上面的这些数据都是添加任务时使用的，下面的在添加任务和添加进度时都使用*/

    /**
     * 任务ID
     */
    private Long questId;

    /**
     * 进度ID
     */
    private Long scheduleId;

    /**
     * 进度标题
     */
    private String scheduleTitle;

    /**
     * 管理员审核
     */
    private Integer adminCheck;

    /**
     * 照片识别
     */
    private Integer photoCheck;

    /**
     * 地点识别
     */
    private Integer locationCheck;

    /**
     * 面部识别（硬件）
     */
    private Integer faceCheck;

    /**
     * 进度启用状态
     */
    private Integer scheduleStatus;

    /**
     * 启用地点 0-禁用 1-启用
     */
    private Integer needLocation;

    /**
     * 地点名称
     */
    private String locationName;

    /**
     * 地点描述
     */
    private String locationDescription;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 地点图片集合
     */
    private List<String> locationPhotoUrls;
}
