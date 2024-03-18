package love.ytlsnb.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.ytlsnb.model.user.dto.UserQueryDTO;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.model.user.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户基本信息持久层
 *
 * @author 金泓宇
 * @date 2024/01/21
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<UserVO> listByConditions(UserQueryDTO userQueryDTO);
}
