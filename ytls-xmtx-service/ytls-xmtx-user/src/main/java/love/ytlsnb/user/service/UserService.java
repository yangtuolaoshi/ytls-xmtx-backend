package love.ytlsnb.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import love.ytlsnb.model.user.dto.UserQueryDTO;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.model.user.dto.UserLoginDTO;
import love.ytlsnb.model.user.dto.UserRegisterDTO;

import java.util.List;

/**
 * 用户基本信息业务层
 *
 * @author 金泓宇
 * @date 2024/01/21
 */
public interface UserService  extends IService<User> {
    User selectByAccount(String account);

    User selectInsensitiveUserById(Long id);

    List<User> list(UserQueryDTO userQueryDTO);


    String  login(UserLoginDTO userLoginDTO, HttpServletRequest request);
    void register(UserRegisterDTO userRegisterDTO);

    boolean sign();

    boolean isSigned();

    void sendShortMessage(String phone) throws Exception;
}
