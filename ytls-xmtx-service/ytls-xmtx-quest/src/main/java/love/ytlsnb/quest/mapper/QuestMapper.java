package love.ytlsnb.quest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.ytlsnb.model.quest.po.Quest;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户基本信息持久层
 *
 * @author ula
 * @date 2024/01/30
 */
@Mapper
public interface QuestMapper extends BaseMapper<Quest> {
}
