package love.ytlsnb.quest.service;

import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.quest.dto.MapFilterDTO;
import love.ytlsnb.model.quest.dto.QuestDTO;
import love.ytlsnb.model.quest.po.QuestSchedule;
import love.ytlsnb.model.quest.vo.QuestScheduleInfoVO;
import love.ytlsnb.model.quest.vo.QuestScheduleVo;

import java.util.List;

/**
 * 任务进度业务层接口
 *
 * @author 金泓宇
 * @date 2024/3/4
 */
public interface QuestScheduleService {
    /**
     * 管理平台查询地图坐标点
     * @param mapFilterDTO 过滤条件
     * @return 坐标点集合
     */
    List<QuestScheduleVo> getAdminMap(MapFilterDTO mapFilterDTO);

    /**
     * 为一个任务添加进度
     * @param questDTO 进度表单数据
     * @return 添加的进度ID
     */
    Long add(QuestDTO questDTO);

    /**
     * 根据ID查询进度详情
     * @param id 主键ID
     * @return 详情结果对象
     */
    QuestScheduleInfoVO getInfoById(Long id);

    /**
     * 根据任务ID查询其下的所有进度
     * @param questId 任务ID
     * @return 结果封装
     */
    PageResult<List<QuestSchedule>> getPageByQuestId(Long questId, int page, int size);

    /**
     * 更新
     * @param questDTO 表单
     * @return 是否更新成功
     */
    Boolean update(QuestDTO questDTO);

    /**
     * 根据ID删除
     * @param id 主键ID
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);

    /**
     * 根据任务ID删除这个任务下的所有进度
     * @param questId 任务ID
     * @return 是否删除成功
     */
    Boolean deleteByQuestId(Long questId);
}
