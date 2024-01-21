package love.ytlsnb.user.service.impl;

import love.ytlsnb.model.user.pojo.User;
import love.ytlsnb.user.mapper.UserMapper;
import love.ytlsnb.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户基本信息业务层实现类
 *
 * @author 金泓宇
 * @date 2024/01/21
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getById(String id) {
        return userMapper.selectById(id);
    }
}
