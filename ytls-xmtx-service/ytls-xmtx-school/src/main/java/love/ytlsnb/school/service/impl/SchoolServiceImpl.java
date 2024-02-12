package love.ytlsnb.school.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import love.ytls.api.quest.QuestClient;
import love.ytlsnb.common.constants.QuestConstant;
import love.ytlsnb.common.constants.RedisConstant;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.UserConstant;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.utils.AdminHolder;
import love.ytlsnb.common.utils.JwtUtil;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.dto.QuestInsertDTO;
import love.ytlsnb.model.quest.po.Quest;
import love.ytlsnb.model.school.po.Admin;
import love.ytlsnb.model.school.po.School;
import love.ytlsnb.model.user.dto.UserLoginDTO;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.school.mapper.SchoolMapper;
import love.ytlsnb.school.service.SchoolService;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ula
 * @date 2024/2/3 14:59
 */
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements SchoolService {
    @Autowired
    private SchoolMapper schoolMapper;
    @Autowired
    private QuestClient questClient;
}
