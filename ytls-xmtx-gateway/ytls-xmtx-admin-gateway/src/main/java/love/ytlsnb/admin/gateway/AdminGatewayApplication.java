package love.ytlsnb.admin.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ula
 * @date 2024/2/7 13:54
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"love.ytlsnb.common.properties", "love.ytlsnb.admin.gateway"})
public class AdminGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminGatewayApplication.class, args);
    }
}
