package love.ytlsnb.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.ytlsnb.model.user.po.UserInfo;

/**
 * @author ula
 * @date 2024/2/4 17:54
 */
public interface UserInfoService extends IService<UserInfo>{
    UserInfo getUserInfo();
}
