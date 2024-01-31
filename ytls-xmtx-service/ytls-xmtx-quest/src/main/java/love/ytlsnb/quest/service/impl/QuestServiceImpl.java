package love.ytlsnb.quest.service.impl;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.constants.RedisConstant;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.UserConstant;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.properties.JwtProperties;
import love.ytlsnb.common.properties.UserProperties;
import love.ytlsnb.common.utils.JwtUtil;
import love.ytlsnb.model.user.dto.UserLoginDTO;
import love.ytlsnb.model.user.dto.UserRegisterDTO;
import love.ytlsnb.model.user.entity.User;
import love.ytlsnb.quest.mapper.QuestMapper;
import love.ytlsnb.quest.service.QuestService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户基本信息业务层实现类
 *
 * @author ula
 * @date 2024/01/30
 */
@Service
@Slf4j
public class QuestServiceImpl implements QuestService {
    @Autowired
    private QuestMapper questMapper;
}
