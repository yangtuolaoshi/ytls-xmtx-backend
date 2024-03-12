package love.ytlsnb.school.test;

import love.ytls.api.quest.QuestClient;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.quest.dto.QuestQueryDTO;
import love.ytlsnb.model.quest.vo.QuestVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class QuestTest {
    @Autowired
    private QuestClient questClient;

    @Test
    void testFeign() {
        PageResult<List<QuestVo>> pageByCondition = questClient.getPageByCondition(new QuestQueryDTO(), 1, 5);
        System.out.println(pageByCondition);
    }
}
