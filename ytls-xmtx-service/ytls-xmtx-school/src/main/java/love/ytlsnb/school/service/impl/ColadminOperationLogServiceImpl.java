package love.ytlsnb.school.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.ytlsnb.common.aop.OperationLogService;
import love.ytlsnb.model.common.OperationLog;
import love.ytlsnb.model.school.po.ColadminOperationLog;
import love.ytlsnb.school.mapper.ColadminOperationLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ula
 * @date 2024/3/15 17:14
 */
@Service
public class ColadminOperationLogServiceImpl extends ServiceImpl<ColadminOperationLogMapper, ColadminOperationLog> implements OperationLogService {
    @Autowired
    ColadminOperationLogMapper coladminOperationLogMapper;

    @Override
    public void addOperationLog(OperationLog operationLog) {
        coladminOperationLogMapper.insert((ColadminOperationLog) operationLog);
    }
}
