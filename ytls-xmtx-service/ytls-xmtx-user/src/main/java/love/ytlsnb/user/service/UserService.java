package love.ytlsnb.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import love.ytlsnb.model.user.dto.*;
import love.ytlsnb.model.user.po.User;
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
public interface UserService extends IService<User> {
    User getByAccount(String account);
    User getByPhone(String phone);

    User getInsensitiveUserById(Long id);

    List<User> list(UserQueryDTO userQueryDTO);


    String login(UserLoginDTO userLoginDTO, HttpServletRequest request);

    void register(UserRegisterDTO userRegisterDTO);

    boolean sign();

    boolean isSigned();

    void sendShortMessage(String phone) throws Exception;

    void addUser(UserInsertDTO userInsertDTO);


    void update(UserUpdateDTO userUpdateDTO);
    void uploadIdCard(String idCard) throws Exception;

    void uploadRealPhoto(String realPhoto) throws Exception;

    void uploadAdmissionLetter(String admissionLetter);

    void addUserBatch(List<UserInsertBatchDTO> userInsertBatchDTOList) throws IOException;

    void updatePassword(UserUpdatePasswordDTO userUpdatePasswordDTO);

    String upload(MultipartFile file);

    Boolean[] listSign();
}
