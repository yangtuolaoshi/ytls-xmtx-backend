package love.ytlsnb.school.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import love.ytlsnb.model.coladmin.dto.ColadminLoginDTO;
import love.ytlsnb.model.coladmin.dto.ColadminRegisterDTO;
import love.ytlsnb.model.coladmin.po.Coladmin;
import love.ytlsnb.model.user.dto.UserInsertDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author ula
 * @date 2024/2/28 20:08
 */
public interface ColadminService extends IService<Coladmin> {
    String  login(ColadminLoginDTO coladminLoginDTO, HttpServletRequest request);

    Coladmin selectInsensitiveAdminById(Long adminId);

    void register(ColadminRegisterDTO coladminRegisterDTO);

}
