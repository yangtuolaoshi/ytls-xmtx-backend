package love.ytlsnb.ad;

import cn.hutool.core.util.RandomUtil;
import love.ytls.api.school.SchoolClient;
import love.ytlsnb.ad.job.AdvertisementJob;
import love.ytlsnb.ad.service.AdvertisementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author ula
 * @date 2024/3/25 22:47
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdTest {
    @Autowired
    @Qualifier("advertisementJobExecutor")
    public ExecutorService advertisementJobExecutor;
    @Autowired
    private SchoolClient client;
    @Autowired
    private AdvertisementService adService;

    @Autowired
    private AdvertisementJob adJob;

    @Test
    public void testJob() throws InterruptedException {
        adJob.calculateAdvertisementSimilarity();
        adJob.calculateRecommendationScore();
    }

    @Test
    public void test2() {
        adService.listByIds(new ArrayList<>());
    }
}
