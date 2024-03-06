package love.ytlsnb.quest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.ytlsnb.model.quest.dto.MapFilterDTO;
import love.ytlsnb.model.quest.po.QuestSchedule;
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
}
