package love.ytlsnb.school.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.ytlsnb.model.user.dto.UserInsertDTO;
import love.ytlsnb.model.user.dto.UserQueryDTO;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.model.user.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author ula
 * @date 2024/3/15 16:44
 */
public interface UserService {
    void addUserBatch(MultipartFile file) throws IOException;

    List<UserVO> listUserByConditions(UserQueryDTO userQueryDTO);

    void addUser(UserInsertDTO userInsertDTO);

    void deleteUserById(Long id);

    void updateUserById(UserInsertDTO userInsertDTO, Long id);

    UserVO getUserVOById(Long id);
}
