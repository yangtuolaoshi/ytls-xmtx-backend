package love.ytlsnb.common.exception;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static love.ytlsnb.common.constants.ResultCodes.SERVER_ERROR;

/**
 * 全局异常处理器
 *
 * @author 金泓宇
 * @date 2024/01/21
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 未知异常
     * @return 表现层数据封装
     */
    @ExceptionHandler(Exception.class)
    public Result<Object> doException(Exception e) {
        e.printStackTrace();
        return Result.fail(SERVER_ERROR, "系统开小差了，请稍后再试...");
    }

    @ExceptionHandler(BusinessException.class)
    public Result<Object> doBusinessException(BusinessException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.fail(e.getCode(), e.getMessage());
    }
}
