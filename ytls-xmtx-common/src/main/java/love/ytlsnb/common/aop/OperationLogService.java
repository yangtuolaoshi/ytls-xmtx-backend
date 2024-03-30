package love.ytlsnb.common.aop;

import love.ytlsnb.model.common.OperationLog;

/**
 * @author ula
 * @date 2024/3/15 17:21
 */
public interface OperationLogService {
    void addOperationLog(OperationLog operationLog);
}
