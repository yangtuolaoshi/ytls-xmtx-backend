package love.ytlsnb.quest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 任务服务启动类
 *
 * @author ula
 * @date 2024/01/30
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@MapperScan("love.ytlsnb.quest.mapper")
@ComponentScan({"love.ytlsnb.common","love.ytlsnb.quest"})
public class QuestApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuestApplication.class, args);
    }
}
