package love.ytlsnb.user.service;

import love.ytlsnb.model.user.pojo.User;
import love.ytlsnb.model.user.pojo.dto.UserRegisterDTO;

/**
 * 用户基本信息业务层
 *
 * @author 金泓宇
 * @date 2024/01/21
 */
public interface UserService {
    /**
     * 根据ID查询单个用户
     * @param id 32位UUID
     * @return 用户基本信息
     */
    User getById(Long id);

    String register(UserRegisterDTO userRegisterDTO);
}
