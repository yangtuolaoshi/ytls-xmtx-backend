package love.ytlsnb.quest.test;

import love.ytlsnb.model.quest.dto.QuestDTO;
import love.ytlsnb.quest.service.QuestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ula
 * @date 2024/2/12 16:01
 */
@SpringBootTest
public class QuestTest {
    @Autowired
    private QuestService questService;

//    @Test
//    public void testGetPageByCondition() {
//        questService.getPageByCondition(null, 0, 5);
//    }
//
//    @Test
//    public void testAdd() {
//        QuestDTO questAddDTO = new QuestDTO();
//        questAddDTO.setQuestTitle("主线任务1");
//        questAddDTO.setType(0);
//        questAddDTO.setQuestStatus(1);
//        System.out.println(questService.add(questAddDTO));
//    }
}
