package love.ytlsnb.quest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.ytlsnb.model.quest.po.ClockInMethod;
import org.apache.ibatis.annotations.Mapper;

/**
 * 打卡方式持久层接口
 *
 * @author 金泓宇
 * @date 2024/3/14
 */
@Mapper
public interface ClockInMethodMapper extends BaseMapper<ClockInMethod> {
}
