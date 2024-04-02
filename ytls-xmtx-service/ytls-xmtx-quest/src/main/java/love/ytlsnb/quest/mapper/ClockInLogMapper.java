package love.ytlsnb.quest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.ytlsnb.model.quest.po.ClockInLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 打卡记录持久层
 *
 * @author 金泓宇
 * @date 2024/3/15
 */
@Mapper
public interface ClockInLogMapper extends BaseMapper<ClockInLog> {
}
