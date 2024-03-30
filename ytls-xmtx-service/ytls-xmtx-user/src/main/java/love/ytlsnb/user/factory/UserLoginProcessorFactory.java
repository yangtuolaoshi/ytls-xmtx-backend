package love.ytlsnb.user.factory;

import love.ytlsnb.model.common.LoginType;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.user.service.UserLoginProcessor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ula
 * @date 2024/3/13 19:50
 */
@Component
public class UserLoginProcessorFactory implements ApplicationContextAware {
    public static Map<LoginType, UserLoginProcessor> userLoginProcessorMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, UserLoginProcessor> beansOfType = applicationContext.getBeansOfType(UserLoginProcessor.class);
        beansOfType.forEach((s, userLoginProcessor) ->
                userLoginProcessorMap.put(userLoginProcessor.getType(), userLoginProcessor));
    }

    public static UserLoginProcessor getUserLoginProcessor(LoginType loginType) {
        if (userLoginProcessorMap == null || userLoginProcessorMap.size() == 0) {
            throw new BusinessException(ResultCodes.SERVER_ERROR, "服务器对象尚未初始化");
        }
        if (!userLoginProcessorMap.containsKey(loginType)) {
            throw new BusinessException(ResultCodes.SERVER_ERROR, "未检测到处理器{" + loginType + "}，请检查相关配置");
        }
        return userLoginProcessorMap.get(loginType);
    }
}