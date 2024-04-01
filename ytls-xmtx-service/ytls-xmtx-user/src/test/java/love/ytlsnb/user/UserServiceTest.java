package love.ytlsnb.user;

import jakarta.servlet.http.HttpServletRequest;
import love.ytlsnb.model.user.dto.UserLoginDTO;
import love.ytlsnb.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ula
 */
@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    HttpServletRequest request;

    @Test
    public void testLogin() {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setAccount("18122542275");
        userLoginDTO.setPassword("123456");
        userService.login(userLoginDTO);
    }
}
