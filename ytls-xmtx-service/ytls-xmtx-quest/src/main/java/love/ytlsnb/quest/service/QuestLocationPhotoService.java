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

    /**
     * 根据ID批量删除
     * @param locationId 图片ID集合
     * @return 是否删除成功
     */
    Boolean deleteByLocationId(Long locationId);

    /**
     * 删除一组地点对应的图片
     * @param locationIds 地点ID集合
     * @return 是否删除成功
     */
    Boolean deleteByLocationIds(List<Long> locationIds);
}
