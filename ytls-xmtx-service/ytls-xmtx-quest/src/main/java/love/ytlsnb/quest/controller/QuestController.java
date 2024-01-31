package love.ytlsnb.quest.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.user.dto.UserLoginDTO;
import love.ytlsnb.model.user.dto.UserRegisterDTO;
import love.ytlsnb.model.user.entity.User;
import love.ytlsnb.quest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

/**
 * 用户基本信息控制器层
 *
 * @author ula
 * @date 2024/01/30
 */
@RestController
@RequestMapping("/quest")
@Slf4j
public class QuestController {
    @Autowired
    private QuestService userService;
}
