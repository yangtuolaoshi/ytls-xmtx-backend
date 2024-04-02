package love.ytlsnb.ad;

import cn.hutool.core.util.RandomUtil;
import love.ytls.api.school.SchoolClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.MathContext;
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

    @Test
    public void testExecutor() throws InterruptedException {
        Long begin1 = System.currentTimeMillis();
        for (int i = 0; i < 5000; i++) {
            BigDecimal sum = BigDecimal.ZERO;
            for (int j = 0; j < 100; j++) {
                sum = sum.add(new BigDecimal(RandomUtil.randomNumbers(5))).sqrt(MathContext.DECIMAL64)
                        .pow(2).sqrt(MathContext.DECIMAL64)
                        .pow(2).sqrt(MathContext.DECIMAL64)
                        .pow(2).sqrt(MathContext.DECIMAL64)
                        .pow(2).sqrt(MathContext.DECIMAL64)
                        .pow(2).sqrt(MathContext.DECIMAL64)
                        .pow(2).sqrt(MathContext.DECIMAL64)
                        .pow(2).sqrt(MathContext.DECIMAL64)
                        .pow(2).sqrt(MathContext.DECIMAL64)
                        .pow(2).sqrt(MathContext.DECIMAL64)
                        .pow(2).sqrt(MathContext.DECIMAL64)
                        .pow(2).sqrt(MathContext.DECIMAL64)
                        .pow(2)
                ;
            }
        }
        Long end1 = System.currentTimeMillis();
        Long begin2 = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(5000);
        for (int i = 0; i < 5000; i++) {
            advertisementJobExecutor.submit(() -> {
                BigDecimal sum = BigDecimal.ZERO;
                for (int j = 0; j < 100; j++) {
                    sum = sum.add(new BigDecimal(RandomUtil.randomNumbers(5))).sqrt(MathContext.DECIMAL64)
                            .pow(2).sqrt(MathContext.DECIMAL64)
                            .pow(2).sqrt(MathContext.DECIMAL64)
                            .pow(2).sqrt(MathContext.DECIMAL64)
                            .pow(2).sqrt(MathContext.DECIMAL64)
                            .pow(2).sqrt(MathContext.DECIMAL64)
                            .pow(2).sqrt(MathContext.DECIMAL64)
                            .pow(2).sqrt(MathContext.DECIMAL64)
                            .pow(2).sqrt(MathContext.DECIMAL64)
                            .pow(2).sqrt(MathContext.DECIMAL64)
                            .pow(2).sqrt(MathContext.DECIMAL64)
                            .pow(2).sqrt(MathContext.DECIMAL64)
                            .pow(2);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        Long end2 = System.currentTimeMillis();
        System.out.println(end1 - begin1);
        System.out.println(end2 - begin2);
    }
    @Test
    public void test2(){
        System.out.println(Byte.parseByte("2")==2);
    }
}
