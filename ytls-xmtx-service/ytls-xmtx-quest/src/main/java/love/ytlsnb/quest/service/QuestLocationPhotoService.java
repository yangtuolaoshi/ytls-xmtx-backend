package love.ytlsnb.quest.service;

import java.util.List;

/**
 * 任务地点图片业务层接口
 *
 * @author 金泓宇
 * @date 2024/3/5
 */
public interface QuestLocationPhotoService {
    /**
     * 根据图片URL集合和地点ID来为这个地点批量添加图片
     * @param urls URL集合
     * @return 是否添加成功
     */
    Boolean addBatchByUrls(List<String> urls, Long locationId);
}
