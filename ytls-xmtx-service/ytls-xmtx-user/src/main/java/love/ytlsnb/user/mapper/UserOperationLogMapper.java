package love.ytlsnb.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.ytlsnb.model.user.po.UserOperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ula
 * @date 2024/3/15 19:21
 */
@Mapper
public interface UserOperationLogMapper extends BaseMapper<UserOperationLog> {
}
