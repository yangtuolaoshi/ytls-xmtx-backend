package love.ytlsnb.common.aop;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import love.ytlsnb.common.constants.OperationConstant;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.utils.ColadminHolder;
import love.ytlsnb.common.utils.UserHolder;
import love.ytlsnb.model.common.LogOperation;
import love.ytlsnb.model.common.Operator;
import love.ytlsnb.model.common.OperationLog;
import love.ytlsnb.model.school.po.Coladmin;
import love.ytlsnb.model.school.po.ColadminOperationLog;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.model.user.po.UserOperationLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author ula
 * @date 2024/3/14 20:00
 */
@Aspect
@Component
@ConditionalOnBean(OperationLogService.class)
public class OperationLogger {
    @Autowired
    OperationLogService operationLogService;

    @Pointcut("@annotation(love.ytlsnb.model.common.LogOperation)")
    private void logOperationPointcut() {
    }

    @Around("logOperationPointcut()")
    public Object logColadminOperation(ProceedingJoinPoint pjp) throws Throwable {
        OperationLog operationLog = null;
        // 获取操作的相关数据
        // 获取Request对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(requestAttributes).getRequest();
        // 获取当前增强的方法签名
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        // 根据注解获取当前的操作者
        LogOperation annotation = methodSignature.getMethod().getAnnotation(LogOperation.class);
        Operator operator = annotation.value();

        StringBuilder detail = new StringBuilder("传入参数:");
        detail.append(Arrays.toString(pjp.getArgs()));
        switch (operator) {
            case USER -> {
                // 记录User相关信息
                operationLog = new UserOperationLog();
                User user = UserHolder.getUser();
                if (user != null) {
                    // 用户登录的操作没有数据
                    operationLog.setOperId(user.getId());
                    operationLog.setUsername(user.getNickname());
                } else {
                    operationLog.setOperId(0L);
                    operationLog.setUsername("");
                }
            }
            case COLADMIN -> {
                // 记录Coladmin相关信息
                operationLog = new ColadminOperationLog();
                Coladmin coladmin = ColadminHolder.getColadmin();
                if (coladmin != null) {
                    // 登录等操作时Holder里面没有数据
                    operationLog.setOperId(coladmin.getId());
                    operationLog.setUsername(coladmin.getUsername());
                } else {
                    operationLog.setOperId(0L);
                    operationLog.setUsername("");
                }
            }
            case ADMIN -> {
                // TODO
            }
            default -> {
                // 默认为空即可
            }
        }
        if (operationLog == null) {
            throw new BusinessException(ResultCodes.SERVER_ERROR, "OperationLogger对象创建异常");
        }
        operationLog.setOperTime(LocalDateTime.now());
        operationLog.setOperUrl(request.getRequestURL().toString());
        operationLog.setOperClazz(methodSignature.getDeclaringTypeName());
        operationLog.setOperMethod(methodSignature.getName());
        operationLog.setOperType(request.getMethod());
        Object obj = null;
        try {
            return obj = pjp.proceed();
        } catch (RuntimeException re) {
            operationLog.setOperStatus(OperationConstant.FAILED);
            operationLog.setErrorMsg(re.getMessage());
            throw re;
        } finally {
            if (operationLog.getOperStatus() == null) {
                operationLog.setOperStatus(OperationConstant.SUCCESS);
                detail.append("\n返回参数:" + JSONUtil.toJsonStr(obj));
                operationLog.setOperDetail(detail.toString());
            }
            operationLogService.addOperationLog(operationLog);
        }
    }
}
