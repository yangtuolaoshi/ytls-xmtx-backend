package love.ytlsnb.quest.service.impl;

import cn.hutool.json.JSONUtil;
import love.ytlsnb.common.utils.UserHolder;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.quest.doc.QuestScheduleDoc;
import love.ytlsnb.quest.service.SearchService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 任务搜索业务层实现类
 *
 * @author 金泓宇
 * @date 2024/3/31
 */
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private RestHighLevelClient client;

    /**
     * 响应解析
     *
     * @param response 响应
     * @param page     页码
     * @param size     每页条数
     * @return 结果
     */
    private PageResult<List<QuestScheduleDoc>> analysisResponse(SearchResponse response, int page, int size) {
        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        LinkedList<QuestScheduleDoc> questScheduleDocs = new LinkedList<>();
        for (SearchHit hit : searchHits) {
            String jsonStr = hit.getSourceAsString();
            QuestScheduleDoc questScheduleDoc = JSONUtil.toBean(jsonStr, QuestScheduleDoc.class);
            // 高亮显示
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields != null && highlightFields.size() > 0) {
                HighlightField questTitleHighlightField = highlightFields.get("questTitle");
                if (questTitleHighlightField != null) {
                    String questTitle = questTitleHighlightField.getFragments()[0].toString();
                    questScheduleDoc.setQuestTitle(questTitle);
                }
                HighlightField objectiveHighlightField = highlightFields.get("objective");
                if (objectiveHighlightField != null) {
                    String objective = objectiveHighlightField.getFragments()[0].toString();
                    questScheduleDoc.setObjective(objective);
                }
                HighlightField questDescriptionHighlightField = highlightFields.get("questDescription");
                if (questDescriptionHighlightField != null) {
                    String questDescription = questDescriptionHighlightField.getFragments()[0].toString();
                    questScheduleDoc.setQuestDescription(questDescription);
                }
                HighlightField tipHighlightField = highlightFields.get("tip");
                if (tipHighlightField != null) {
                    String tip = tipHighlightField.getFragments()[0].toString();
                    questScheduleDoc.setTip(tip);
                }
                HighlightField requiredItemHighlightField = highlightFields.get("requiredItem");
                if (requiredItemHighlightField != null) {
                    String requiredItem = requiredItemHighlightField.getFragments()[0].toString();
                    questScheduleDoc.setRequiredItem(requiredItem);
                }
                HighlightField questScheduleTitleHighlightField = highlightFields.get("questScheduleTitle");
                if (questScheduleTitleHighlightField != null) {
                    String questScheduleTitle = questScheduleTitleHighlightField.getFragments()[0].toString();
                    questScheduleDoc.setQuestScheduleTitle(questScheduleTitle);
                }
                if (questScheduleDoc.getNeedLocation() == 1) {
                    HighlightField locationNameHighlightField = highlightFields.get("locationName");
                    if (locationNameHighlightField != null) {
                        String locationName = locationNameHighlightField.getFragments()[0].toString();
                        questScheduleDoc.setLocationName(locationName);
                    }
                    HighlightField locationDescriptionHighlightField = highlightFields.get("locationDescription");
                    if (locationDescriptionHighlightField != null) {
                        String locationDescription = locationDescriptionHighlightField.getFragments()[0].toString();
                        questScheduleDoc.setLocationDescription(locationDescription);
                    }
                }
            }
            questScheduleDocs.add(questScheduleDoc);
        }
        return new PageResult<>(page, size, questScheduleDocs, hits.getTotalHits().value);
    }

    @Override
    public PageResult<List<QuestScheduleDoc>> search(String keyword, int page, int size) throws IOException {
        Long schoolId = UserHolder.getUser().getSchoolId();
        // 1. 构造请求对象
        SearchRequest request = new SearchRequest("quest_schedule");
        // 2. 搜索条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("selectCondition", keyword))
                        .must(QueryBuilders.termQuery("schoolId", schoolId));
        request.source()
                .query(boolQueryBuilder).from((page - 1) * size).size(size)
                .highlighter(new HighlightBuilder()
                        .field("questTitle")
                        .field("objective")
                        .field("questDescription")
                        .field("tip")
                        .field("requiredItem")
                        .field("questScheduleTitle")
                        .field("locationName")
                        .field("locationDescription")
                        .preTags("<View style={{color: 'red'}}>")
                        .postTags("</View>")
                        .requireFieldMatch(false)
                );
        // 3. 发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 4. 请求解析
        return analysisResponse(response, page, size);
    }
}
