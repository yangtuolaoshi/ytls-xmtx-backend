package love.ytlsnb.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 用户基本信息服务启动类
 *
 * @author 金泓宇
 * @date 2024/01/21
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("love.ytlsnb.user.mapper")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
