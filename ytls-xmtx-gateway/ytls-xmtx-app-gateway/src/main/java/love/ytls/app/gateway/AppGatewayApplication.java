package love.ytls.app.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 用户APP网关启动类
 *
 * @author 金泓宇
 * @date 2024/01/21
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"love.ytlsnb.common","love.ytls.app.gateway"})
@EnableFeignClients(basePackages = "love.ytls.api.user")
public class AppGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppGatewayApplication.class, args);
    }
}
