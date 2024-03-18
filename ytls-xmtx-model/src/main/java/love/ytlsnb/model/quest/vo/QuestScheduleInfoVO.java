package love.ytlsnb.model.quest.vo;

import lombok.Data;
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
