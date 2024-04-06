package love.ytlsnb.quest.test;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import love.ytlsnb.common.utils.UserHolder;
import love.ytlsnb.model.quest.doc.QuestScheduleDoc;
import love.ytlsnb.model.quest.dto.QuestDTO;
import love.ytlsnb.model.quest.po.Quest;
import love.ytlsnb.model.quest.po.QuestInfo;
import love.ytlsnb.model.quest.po.QuestLocation;
import love.ytlsnb.model.quest.po.QuestSchedule;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.quest.mapper.QuestInfoMapper;
import love.ytlsnb.quest.mapper.QuestLocationMapper;
import love.ytlsnb.quest.mapper.QuestMapper;
import love.ytlsnb.quest.mapper.QuestScheduleMapper;
import love.ytlsnb.quest.service.SearchService;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

/**
 * 搜索功能测试
 */
@SpringBootTest
public class ESTest {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private QuestMapper questMapper;

    @Autowired
    private QuestInfoMapper questInfoMapper;

    @Autowired
    private QuestScheduleMapper questScheduleMapper;

    @Autowired
    private QuestLocationMapper questLocationMapper;

//    /**
//     * 导入数据
//     * @throws IOException IOException
//     */
//    @Test
//    void addData() throws IOException {
////        QuestScheduleDoc questScheduleDoc = new QuestScheduleDoc();
////        // 进度
////        QuestSchedule questSchedule = questScheduleMapper.selectById(1768180079332511745L);
////        BeanUtil.copyProperties(questSchedule, questScheduleDoc);
////        questScheduleDoc.setQuestScheduleTitle(questSchedule.getScheduleTitle());
////        // 任务
////        Quest quest = questMapper.selectById(questSchedule.getQuestId());
////        BeanUtil.copyProperties(quest, questScheduleDoc);
////        // 任务详情
////        QuestInfo questInfo = questInfoMapper.selectById(quest.getInfoId());
////        BeanUtil.copyProperties(questInfo, questScheduleDoc);
////        // 地点
////        if (questSchedule.getNeedLocation() == 1) {
////            QuestLocation questLocation = questLocationMapper.selectById(questSchedule.getLocationId());
////            BeanUtil.copyProperties(questLocation, questScheduleDoc);
////            questScheduleDoc.setQuestLocationId(questLocation.getId());
////        }
////        questScheduleDoc.setQuestId(quest.getId());
////        questScheduleDoc.setQuestScheduleId(questSchedule.getId());
////        // 1. 创建请求对象
////        IndexRequest request = new IndexRequest("quest_schedule").id(questSchedule.getId().toString());
////        // 2. 转json
////        String jsonStr = JSONUtil.toJsonStr(questScheduleDoc);
////        request.source(jsonStr, XContentType.JSON);
////        // 3. 发送请求
////        restHighLevelClient.index(request, RequestOptions.DEFAULT);
//
//        List<QuestSchedule> questSchedules = questScheduleMapper.selectList(null);
//        for (QuestSchedule questSchedule : questSchedules) {
//            QuestScheduleDoc questScheduleDoc = new QuestScheduleDoc();
//            // 进度
//            BeanUtil.copyProperties(questSchedule, questScheduleDoc);
//            questScheduleDoc.setQuestScheduleTitle(questSchedule.getScheduleTitle());
//            // 任务
//            Quest quest = questMapper.selectById(questSchedule.getQuestId());
//            BeanUtil.copyProperties(quest, questScheduleDoc);
//            // 任务详情
//            QuestInfo questInfo = questInfoMapper.selectById(quest.getInfoId());
//            BeanUtil.copyProperties(questInfo, questScheduleDoc);
//            // 地点
//            if (questSchedule.getNeedLocation() == 1) {
//                QuestLocation questLocation = questLocationMapper.selectById(questSchedule.getLocationId());
//                BeanUtil.copyProperties(questLocation, questScheduleDoc);
//                questScheduleDoc.setQuestLocationId(questLocation.getId());
//            }
//            questScheduleDoc.setQuestId(quest.getId());
//            questScheduleDoc.setQuestScheduleId(questSchedule.getId());
//            // 1. 创建请求对象
//            IndexRequest request = new IndexRequest("quest_schedule").id(questSchedule.getId().toString());
//            // 2. 转json
//            String jsonStr = JSONUtil.toJsonStr(questScheduleDoc);
//            request.source(jsonStr, XContentType.JSON);
//            // 3. 发送请求
//            restHighLevelClient.index(request, RequestOptions.DEFAULT);
//        }
//    }
//
//    @Autowired
//    private SearchService searchService;
//
//    @Test
//    void testSearch() throws IOException {
//        User user = new User();
//        user.setSchoolId(1L);
//        UserHolder.saveUser(user);
//        List<QuestScheduleDoc> questScheduleDocs = searchService.search("寻找", 1, 5).getData();
//        for (QuestScheduleDoc questScheduleDoc : questScheduleDocs) {
//            System.out.println(questScheduleDoc);
//        }
//    }
}
