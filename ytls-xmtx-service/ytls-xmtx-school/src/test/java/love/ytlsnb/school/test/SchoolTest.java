package love.ytlsnb.school.test;

import love.ytlsnb.school.service.SchoolService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ula
 * @date 2024/2/7 15:27
 */
@SpringBootTest
public class SchoolTest {
    @Autowired
    private SchoolService schoolService;

    @Test
    public void test(){
        schoolService.list();
    }
}
