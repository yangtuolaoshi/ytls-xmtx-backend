package love.ytlsnb.coladmin.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ula
 * @date 2024/2/28 19:33
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"love.ytlsnb.common.properties", "love.ytlsnb.coladmin.gateway"})
public class ColadminGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ColadminGatewayApplication.class, args);
    }
}
