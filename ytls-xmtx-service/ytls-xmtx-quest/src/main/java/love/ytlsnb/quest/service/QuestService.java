package love.ytlsnb.quest.service;

import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.quest.dto.QuestDTO;
import love.ytlsnb.model.quest.dto.QuestQueryDTO;
import love.ytlsnb.model.quest.vo.QuestInfoVo;
import love.ytlsnb.model.quest.vo.QuestVo;

import java.util.List;

/**
 * 任务业务层接口
 *
 * @author 金泓宇
 * @author 2024/2/29
 */
public interface QuestService {
    /**
     * 分页条件查询
     * @param page 页码
     * @param size 每页条数
     * @return 分页查询集合
     */
    PageResult<List<QuestVo>> getPageByCondition(QuestQueryDTO questQueryDTO, int page, int size);

    /**
     * 根据ID查询详情
     * @param id 主键ID
     * @return 任务详细信息
     */
    QuestInfoVo getInfoById(Long id);

    /**
     * 添加一个任务，并提供一个任务进度
     * @param questAddDTO 添加任务表单数据
     * @return 新增的主键ID
     */
    Long add(QuestDTO questAddDTO);

    /**
     * 修改任务信息
     * @param questUpdateDTO 修改任务表单数据
     * @return 是否更新成功
     */
    Boolean update(QuestDTO questUpdateDTO);

    /**
     * 根据ID删除
     * @param id 主键ID
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);
}
