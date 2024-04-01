package love.ytlsnb.model.quest.vo;

import lombok.Data;
import love.ytlsnb.model.quest.po.ClockInMethod;
import love.ytlsnb.model.quest.po.QuestLocationPhoto;

import java.util.List;

/**
 * 进度详情结果封装
 *
 * @author 金泓宇
 * @date 2024/3/4
 */
@Data
public class QuestScheduleInfoVO {
    /**
     * 任务标题
     */
    private String questTitle;

    /**
     * 进度标题
     */
    private String scheduleTitle;

    /**
     * 打卡方式
     */
    private List<ClockInMethod> clockInMethods;

    /**
     * 启用地点
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
     * 地点经度
     */
    private Double longitude;

    /**
     * 地点纬度
     */
    private Double latitude;

    /**
     * 地点照片集合
     */
    private List<QuestLocationPhoto> locationPhotos;
}
