package love.ytlsnb.user.service;

import jakarta.servlet.http.HttpServletRequest;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.model.user.dto.UserLoginDTO;
import love.ytlsnb.model.user.dto.UserRegisterDTO;

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
    User selectById(Long id);
    User selectByAccount(String account);

    User selectInsensitiveUserById(Long id);


    String  login(UserLoginDTO userLoginDTO, HttpServletRequest request);
    void register(UserRegisterDTO userRegisterDTO);
}
