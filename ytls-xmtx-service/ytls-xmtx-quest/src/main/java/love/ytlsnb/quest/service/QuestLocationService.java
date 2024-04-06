package love.ytlsnb.quest.service;

import love.ytlsnb.model.quest.vo.QuestLocationInfoVO;

/**
 * 任务地点业务层接口
 *
 * @author 金泓宇
 * @date 2024/3/12
 */
public interface QuestLocationService {
    /**
     * 查询进度的地点信息
     * @param scheduleId 进度ID
     * @return 进度的地点信息
     */
    QuestLocationInfoVO getQuestSchedulePageLocation(Long scheduleId);
}
