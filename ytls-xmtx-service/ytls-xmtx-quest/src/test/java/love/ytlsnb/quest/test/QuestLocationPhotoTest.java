package love.ytlsnb.quest.test;

import love.ytlsnb.model.quest.po.QuestLocation;
import love.ytlsnb.model.quest.po.QuestLocationPhoto;
import love.ytlsnb.quest.mapper.QuestLocationPhotoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.LinkedList;

@SpringBootTest
public class QuestLocationPhotoTest {
    @Autowired
    private QuestLocationPhotoMapper questLocationPhotoMapper;

//    @Test
//    public void testInsertBatch() {
//        LinkedList<QuestLocationPhoto> questLocationPhotos = new LinkedList<>();
//        for (int i = 0; i < 5; i++) {
//            QuestLocationPhoto questLocationPhoto = new QuestLocationPhoto();
//            questLocationPhoto.setId(i + 1L);
//            questLocationPhoto.setLocationId(1L);
//            questLocationPhoto.setUrl("url" + (i + 1));
//            questLocationPhoto.setCreateTime(LocalDateTime.now());
//            questLocationPhoto.setIsDeleted(0);
//            questLocationPhotos.add(questLocationPhoto);
//        }
//        System.out.println(questLocationPhotoMapper.insertBatchSomeColumn(questLocationPhotos));
//    }
}
