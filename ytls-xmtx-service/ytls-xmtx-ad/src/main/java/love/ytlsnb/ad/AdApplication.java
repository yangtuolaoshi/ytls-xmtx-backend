package love.ytlsnb.ad;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author ula
 * @date 2024/3/14 11:32
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@MapperScan("love.ytlsnb.ad.mapper")
@EnableFeignClients(basePackages = "love.ytls.api")
@ComponentScan({"love.ytlsnb.common", "love.ytlsnb.ad"})
public class AdApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdApplication.class,args);
    }
}
