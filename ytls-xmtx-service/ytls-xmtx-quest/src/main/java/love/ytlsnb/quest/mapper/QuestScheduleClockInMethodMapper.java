package love.ytlsnb.quest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.ytlsnb.model.quest.po.QuestScheduleClockInMethod;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务进度打卡方式关联持久层
 *
 * @author 金泓宇
 * @date 2024/3/14
 */
@Mapper
public interface QuestScheduleClockInMethodMapper extends BaseMapper<QuestScheduleClockInMethod> {
}
