package love.ytlsnb.quest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.ytlsnb.model.quest.po.QuestLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务完成记录持久层
 *
 * @author 金泓宇
 * @date 2024/3/12
 */
@Mapper
public interface QuestLogMapper extends BaseMapper<QuestLog> {
}
