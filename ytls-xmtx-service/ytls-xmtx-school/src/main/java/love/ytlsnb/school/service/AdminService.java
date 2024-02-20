package love.ytlsnb.school.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import love.ytlsnb.model.school.dto.AdminLoginDTO;
import love.ytlsnb.model.school.dto.AdminRegisterDTO;
import love.ytlsnb.model.school.po.Admin;
import love.ytlsnb.model.user.dto.UserInsertDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author ula
 * @date 2024/2/6 16:27
 */
public interface AdminService extends IService<Admin> {
    String  login(AdminLoginDTO adminLoginDTO, HttpServletRequest request);

    Admin selectInsensitiveAdminById(Long adminId);

    void register(AdminRegisterDTO adminRegisterDTO);


    void addUser(UserInsertDTO userInsertDTO) throws Exception;

    void addUserBatch(MultipartFile multipartFile) throws IOException;
}
