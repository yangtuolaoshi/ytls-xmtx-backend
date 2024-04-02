package love.ytlsnb.quest.service;

import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.quest.doc.QuestScheduleDoc;

import java.io.IOException;
import java.util.List;

/**
 * 任务搜索业务层接口
 *
 * @author 金泓宇
 * @date 2024/3/31
 */
public interface SearchService {
    /**
     * 关键词搜索
     * @param keyword 管检测
     * @param page 页码
     * @param size 每页条数
     * @return 搜索结果
     */
    PageResult<List<QuestScheduleDoc>> search(String keyword, int page, int size) throws IOException;
}
