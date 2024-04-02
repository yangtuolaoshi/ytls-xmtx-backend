package love.ytlsnb.quest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.ytlsnb.model.quest.po.QuestScheduleLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 进度完成情况持久层
 *
 * @author 金泓宇
 * @date 2024/3/12
 */
@Mapper
public interface QuestScheduleLogMapper extends BaseMapper<QuestScheduleLog> {
}
