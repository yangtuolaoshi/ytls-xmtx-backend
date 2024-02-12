package love.ytlsnb.app.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 用户APP网关启动类
 *
 * @author 金泓宇
 * @date 2024/01/21
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"love.ytlsnb.common.properties","love.ytlsnb.app.gateway"})
public class AppGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppGatewayApplication.class, args);
    }
}
