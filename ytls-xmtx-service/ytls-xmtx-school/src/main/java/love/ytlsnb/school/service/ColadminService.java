package love.ytlsnb.school.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import love.ytlsnb.model.school.dto.ColadminLoginDTO;
import love.ytlsnb.model.school.dto.ColadminRegisterDTO;
import love.ytlsnb.model.school.po.Coladmin;

/**
 * @author ula
 * @date 2024/2/28 20:08
 */
public interface ColadminService extends IService<Coladmin> {
    String  login(ColadminLoginDTO coladminLoginDTO, HttpServletRequest request);

    Coladmin selectInsensitiveAdminById(Long adminId);

    void register(ColadminRegisterDTO coladminRegisterDTO);

}
