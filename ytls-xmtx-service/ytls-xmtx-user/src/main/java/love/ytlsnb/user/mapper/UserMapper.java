package love.ytlsnb.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.ytlsnb.model.user.po.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户基本信息持久层
 *
 * @author 金泓宇
 * @date 2024/01/21
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
