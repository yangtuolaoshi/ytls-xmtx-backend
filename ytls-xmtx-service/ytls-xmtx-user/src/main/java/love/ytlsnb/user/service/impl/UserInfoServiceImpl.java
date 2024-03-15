package love.ytlsnb.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.ytlsnb.common.utils.UserHolder;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.model.user.po.UserInfo;
import love.ytlsnb.user.mapper.UserInfoMapper;
import love.ytlsnb.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ula
 * @date 2024/2/4 17:54
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfo getUserInfo() {
        User user = UserHolder.getUser();
        Long userInfoId = user.getUserInfoId();
        return userInfoMapper.selectById(userInfoId);
    }
}
