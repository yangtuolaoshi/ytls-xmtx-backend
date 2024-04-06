package love.ytlsnb.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.ytlsnb.model.user.po.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ula
 * @date 2024/2/4 17:55
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
