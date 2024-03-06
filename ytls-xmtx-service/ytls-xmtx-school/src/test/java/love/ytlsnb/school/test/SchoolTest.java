package love.ytlsnb.school.test;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import love.ytls.api.user.UserClient;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.school.service.SchoolService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
        Result<User> byId = userClient.getById(1758018823723806722L);
    }
}
