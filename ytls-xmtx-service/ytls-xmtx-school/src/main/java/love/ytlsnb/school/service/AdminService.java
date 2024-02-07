package love.ytlsnb.school.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import love.ytlsnb.model.school.dto.AdminLoginDTO;
import love.ytlsnb.model.school.po.Admin;

/**
 * @author ula
 * @date 2024/2/6 16:27
 */
public interface AdminService extends IService<Admin> {
    String  login(AdminLoginDTO adminLoginDTO, HttpServletRequest request);

    Admin selectInsensitiveAdminById(Long adminId);
}
