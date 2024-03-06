package love.ytlsnb.model.quest.dto;

import lombok.Data;

import java.util.List;

/**
 * 任务进度表单数据封装（弃用，因为存在代码重复问题）
 *
 * @author 金泓宇
 * @date 2024/3/4
 */
@Data
public class QuestScheduleDTO {
    /**
     * 进度ID
     */
    private Long scheduleId;

    /**
     * 所属任务ID
     */
    private Long questId;

    /**
     * 进度标题
     */
    private String title;

    /**
     * 打卡方式
     */
    private Integer clockInMethod;

    /**
     * 是否启用地点
     */
    private Integer needLocation;

    /**
     * 启用状态
     */
    private Integer scheduleStatus;

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
    private List<String> locationPhotos;
}
