package love.ytlsnb.school.test;


import love.ytls.api.user.UserClient;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.school.service.SchoolService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ula
 * @date 2024/2/7 15:27
 */
@SpringBootTest
public class SchoolTest {
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private UserClient userClient;
    @Test
    public void test() {
        Result<User> byId = userClient.getUserById(1758018823723806722L);
        //Result<User> byId = userClient.getById(1758018823723806722L);
    }
}
