package love.ytlsnb.quest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.ytlsnb.model.quest.dto.MapFilterDTO;
import love.ytlsnb.model.quest.po.QuestSchedule;
import love.ytlsnb.model.quest.vo.QuestScheduleCompletionVO;
import love.ytlsnb.model.quest.vo.QuestScheduleMapPoint;
import love.ytlsnb.model.quest.vo.QuestScheduleVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestScheduleMapper extends BaseMapper<QuestSchedule> {
    /**
     * 管理平台获取地图坐标点
     * @param mapFilterDTO 过滤条件
     * @param schoolId 学校ID
     * @return 坐标点集合
     */
    List<QuestScheduleVo> getAdminMap(MapFilterDTO mapFilterDTO, Long schoolId);

    /**
     * 查询某用户对于某任务的进度
     * @param questId 任务ID
     * @param userId 用户ID
     * @return 该用户这个任务的进度
     */
    List<QuestScheduleCompletionVO> getByQuestAndUserId(Long questId, Long userId);

    /**
     * 查询地图坐标点，排除已完成的进度
     * @param questId 任务ID
     * @param userId 用户ID
     * @return 地图坐标点集合
     */
    List<QuestScheduleMapPoint> getMapByQuestAndUserId(Long questId, Long userId);
}
