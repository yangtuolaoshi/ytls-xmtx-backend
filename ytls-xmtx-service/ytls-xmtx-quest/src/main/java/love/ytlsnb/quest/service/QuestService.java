package love.ytlsnb.quest.service;

import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.quest.dto.QuestDTO;
import love.ytlsnb.model.quest.dto.QuestQueryDTO;
import love.ytlsnb.model.quest.po.Quest;
import love.ytlsnb.model.quest.vo.QuestInfoPageVO;
import love.ytlsnb.model.quest.vo.QuestInfoVo;
import love.ytlsnb.model.quest.vo.QuestListItemVO;
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
     * 获取本校的所有任务
     * @param schoolId 学校ID
     * @return 本校的所有任务
     */
    List<Quest> getAll(Long schoolId);

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
     * @param schoolId 学校ID
     * @return 是否删除成功
     */
    Boolean deleteById(Long id, Long schoolId);

    /**
     * 根据ID获取详细信息，包括进度的完成情况
     * @param id 任务ID
     * @return 任务的详细信息
     */
    QuestInfoPageVO getQuestInfoById(Long id);

    /**
     * 获取当前用户正在进行中的任务
     * @param type 任务类型
     * @return 进行中的任务集合
     */
    List<QuestListItemVO> getOngoingQuests(Integer type);

    /**
     * 获取当前用户已完成的任务
     * @param type 任务类型
     * @return 已完成的任务集合
     */
    List<QuestListItemVO> getFinishedQuests(Integer type);

    /**
     * 获取未开始的任务
     * @param type 任务类型
     * @return 未开始的任务集合
     */
    List<QuestListItemVO> getUnstartedQuests(Integer type);
}
