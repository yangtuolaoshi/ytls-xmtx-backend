package love.ytlsnb.school.test;

import cn.hutool.core.thread.ThreadFactoryBuilder;
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
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = new ThreadPoolExecutor(
            3, 10,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), new ThreadFactoryBuilder()
            .setNamePrefix("cache-pool").build(), new ThreadPoolExecutor.AbortPolicy());
    @Test
    public void test() throws InterruptedException {

    }
}
