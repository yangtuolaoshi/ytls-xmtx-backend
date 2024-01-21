package love.ytlsnb.colgadmin.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 学校管理平台网关启动类
 *
 * @author 金泓宇
 * @date 2024/01/21
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ColgAdminGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ColgAdminGatewayApplication.class, args);
    }
}
