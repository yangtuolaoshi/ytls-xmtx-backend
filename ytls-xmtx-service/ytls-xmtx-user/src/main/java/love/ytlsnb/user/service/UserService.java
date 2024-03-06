package love.ytlsnb.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import love.ytlsnb.model.user.dto.UserInsertDTO;
import love.ytlsnb.model.user.dto.UserQueryDTO;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.model.user.dto.UserLoginDTO;
import love.ytlsnb.model.user.dto.UserRegisterDTO;
import love.ytlsnb.model.user.po.UserInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    void addUser(UserInsertDTO userInsertDTO);

    void saveUserAndUserInfoBatch(List<User> userList, List<UserInfo> userInfoList);

    void uploadIdCard(String idCard) throws Exception;

    void uploadRealPhoto(String realPhoto) throws Exception;

    void uploadAdmissionLetter(String admissionLetter);

    void addUserBatch(MultipartFile multipartFile) throws IOException;
}
