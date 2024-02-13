package love.ytlsnb.quest.test;

import cn.hutool.core.bean.BeanUtil;
import love.ytlsnb.common.constants.PojoConstant;
import love.ytlsnb.model.quest.vo.QuestVO;
import love.ytlsnb.model.school.po.Location;
import love.ytlsnb.model.school.po.LocationPhoto;
import love.ytlsnb.model.school.vo.LocationVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

/**
 * @author ula
 * @date 2024/2/12 16:01
 */
public class QuestTest {
    @Test
    public void test1(){
        LocationVO locationVO = new LocationVO();
        QuestVO questVO = new QuestVO();
        ArrayList<String> photos = new ArrayList<>();
        photos.add("aaa");
        photos.add("bbb");
        photos.add("ccc");
        locationVO.setPhotos(photos);
        locationVO.setId(13413541L);

        BeanUtil.copyProperties(locationVO,questVO);
        System.out.println(locationVO);
        System.out.println(questVO);
    }
}
