package love.ytlsnb.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.ytlsnb.common.aop.OperationLogService;
import love.ytlsnb.model.common.OperationLog;
import love.ytlsnb.model.user.po.UserOperationLog;
import love.ytlsnb.user.mapper.UserOperationLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ula
 * @date 2024/3/15 19:22
 */
@Service
public class UserOperationLogServiceImpl extends ServiceImpl<UserOperationLogMapper, UserOperationLog> implements OperationLogService {
    @Autowired
    private UserOperationLogMapper userOperationLogMapper;

    @Override
    public void addOperationLog(OperationLog operationLog) {
        userOperationLogMapper.insert((UserOperationLog) operationLog);
    }
}
