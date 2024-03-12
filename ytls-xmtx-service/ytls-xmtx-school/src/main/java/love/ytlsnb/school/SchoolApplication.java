package love.ytlsnb.school;

import love.ytlsnb.school.config.FeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 在用户服务后启动
 *
 * @author ula
 * @date 2024/2/3 15:08
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableTransactionManagement
@EnableFeignClients(basePackages = "love.ytls.api", defaultConfiguration = FeignConfig.class)
@MapperScan("love.ytlsnb.school.mapper")
@ComponentScan({"love.ytlsnb.common", "love.ytlsnb.school"})
public class SchoolApplication {
    public static void main(String[] args) {
        SpringApplication.run(SchoolApplication.class, args);
    }
}

