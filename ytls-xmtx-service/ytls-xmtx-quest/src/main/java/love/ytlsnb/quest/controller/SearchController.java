package love.ytlsnb.quest.controller;

import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.doc.QuestScheduleDoc;
import love.ytlsnb.quest.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * 任务搜索功能表现层
 */
@RestController
@RequestMapping("/quest/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping
    public PageResult<List<QuestScheduleDoc>> search(String keyword, int page, int size) throws IOException {
        return searchService.search(keyword, page, size);
    }
}
