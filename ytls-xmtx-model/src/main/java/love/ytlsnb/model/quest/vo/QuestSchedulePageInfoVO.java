package love.ytlsnb.model.quest.vo;

import lombok.Data;
import love.ytlsnb.model.quest.po.ClockInLog;
import love.ytlsnb.model.quest.po.ClockInMethod;

import java.util.List;

/**
 * 进度详情页
 *
 * @author 金泓宇
 * @date 2024/3/12
 */
@Data
public class QuestSchedulePageInfoVO {
    /**
     * 进度ID
     */
    private Long scheduleId;

    /**
     * 进度标题
     */
    private String scheduleTitle;

    /**
     * 该进度的打卡记录
     */
    private List<ClockInLog> clockInLogs;

    /**
     * 是否启用地点
     */
    private Integer needLocation;

    /**
     * 该进度的打卡方式
     */
    private List<ClockInMethod> ClockInMethods;

    /**
     * 是否已完成
     * 这个字段在每次打卡时判断是否填入
     */
    private Integer isFinished;
}
