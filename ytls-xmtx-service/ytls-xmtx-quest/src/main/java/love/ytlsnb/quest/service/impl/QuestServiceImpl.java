package love.ytlsnb.quest.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.quest.dto.QuestDTO;
import love.ytlsnb.model.quest.dto.QuestQueryDTO;
import love.ytlsnb.model.quest.po.*;
import love.ytlsnb.model.quest.vo.QuestInfoVo;
import love.ytlsnb.model.quest.vo.QuestVo;
import love.ytlsnb.quest.mapper.*;
import love.ytlsnb.quest.service.QuestLocationPhotoService;
import love.ytlsnb.quest.service.QuestScheduleService;
import love.ytlsnb.quest.service.QuestService;
import love.ytlsnb.quest.utils.QuestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static love.ytlsnb.common.constants.ResultCodes.*;

/**
 * 任务业务层实现类
 *
 * @author 金泓宇
 * @author 2024/2/29
 */
@Service
@Slf4j
public class QuestServiceImpl implements QuestService {
    @Autowired
    private QuestMapper questMapper;

    @Autowired
    private QuestInfoMapper questInfoMapper;

    @Autowired
    private QuestScheduleMapper questScheduleMapper;

    @Autowired
    private QuestLocationMapper questLocationMapper;

    @Autowired
    private QuestScheduleService questScheduleService;

    @Autowired
    private QuestLocationPhotoService questLocationPhotoService;

    @Override
    public PageResult<List<QuestVo>> getPageByCondition(QuestQueryDTO questQueryDTO, int page, int size) {
        // TODO 按照学校ID查询
        // 查询总数
        LambdaQueryWrapper<Quest> queryWrapper = new LambdaQueryWrapper<>();
        Integer type = questQueryDTO.getType();
        if (type != null) {
            queryWrapper.eq(Quest::getType, type);
        }
        String title = questQueryDTO.getTitle();
        if (title != null && !"".equals(title)) {
            queryWrapper.likeRight(Quest::getQuestTitle, title);// 百分号只在右侧，防止索引失效
        }
        Long total = questMapper.selectCount(queryWrapper);
        // 分页查询
        if (title != null) {
            questQueryDTO.setTitle(title + "%");
        }
        List<QuestVo> questVos = questMapper.getPageByCondition(questQueryDTO, (page - 1) * size, size);
        PageResult<List<QuestVo>> pageResult = new PageResult<>(page, questVos.size(), total);
        pageResult.setData(questVos);
        return pageResult;
    }

    @Override
    public QuestInfoVo getInfoById(Long id) {
        Quest quest = questMapper.selectById(id);
        if (quest == null) {
            throw new BusinessException(NOT_FOUND, "任务不存在！");
        }
        QuestInfo questInfo = questInfoMapper.selectById(quest.getInfoId());
        if (questInfo == null) {
            throw new BusinessException(NOT_FOUND, "任务不存在！");
        }
        // TODO 查询总进度数
        QuestInfoVo questInfoVo = new QuestInfoVo();
        // 查询父任务标题
        Long parentId = quest.getParentId();
        if (parentId != null) {
            Quest preQuest = questMapper.selectById(parentId);
            if (preQuest == null) {
                throw new BusinessException(SERVER_ERROR, "数据非法，请联系管理员");
            }
            questInfoVo.setPreQuestTitle(preQuest.getQuestTitle());
        }
        BeanUtil.copyProperties(questInfo, questInfoVo);
        BeanUtil.copyProperties(quest, questInfoVo);
        return questInfoVo;
    }

    /**
     * 任务参数检测
     * @param questDTO 任务添加表单
     */
    private void checkQuestParams(QuestDTO questDTO) {
        // 标题
        String questTitle = questDTO.getQuestTitle();
        if (questTitle == null || "".equals(questTitle)) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "请输入任务标题");
        }
        if (questTitle.length() > 16) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "任务标题不能超过16个字符");
        }
//        LambdaQueryWrapper<Quest> titleQueryWrapper = new LambdaQueryWrapper<>();
//        titleQueryWrapper.eq(Quest::getTitle, questTitle);
//        if (questMapper.selectOne(titleQueryWrapper) != null) {
//            throw new BusinessException(UNPROCESSABLE_ENTITY, "任务标题重复，换个标题吧");
//        }
        // 前置任务
        Long preQuestId = questDTO.getPreQuestId();
        if (preQuestId != null) {
            LambdaQueryWrapper<Quest> preQuestQueryWrapper = new LambdaQueryWrapper<>();
            preQuestQueryWrapper.eq(Quest::getId, preQuestId);
            if (questMapper.selectOne(preQuestQueryWrapper) == null) {
                throw new BusinessException(UNPROCESSABLE_ENTITY, "前置任务不存在");
            }
        }
        // 任务类型
        Integer type = questDTO.getType();
        if (type == null) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "请选择任务类型");
        }
        if (type < 0 || type > 3) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "任务类型非法");
        }
        // 任务目标
        String objective = questDTO.getObjective();
        if (objective == null || "".equals(objective)) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "请输入任务目标");
        }
        if (objective.length() > 64) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "任务目标不能超过64个字符");
        }
        // 任务详情
        String questDescription = questDTO.getQuestDescription();
        if (questDescription != null && questDescription.length() > 256) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "任务详情不能超过256个字符");
        }
        // 准备物品
        String requiredItem = questDTO.getRequiredItem();
        if (requiredItem != null && requiredItem.length() > 64) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "准备物品不能超过64个字符");
        }
        // 任务提示
        String tip = questDTO.getTip();
        if (tip != null && tip.length() > 64) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "任务提示不能超过64个字符");
        }
        // 任务奖励
        Integer reward = questDTO.getReward();
        if (reward == null) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "请设置任务完成奖励");
        }
        if (reward < 0) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "奖励数据非法");
        }
        // 启用状态
        Integer questStatus = questDTO.getQuestStatus();
        if (questStatus == null || questStatus > 1 || questStatus < 0) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "请选择启用状态");
        }
    }

    @Transactional
    @Override
    public Long add(QuestDTO questDTO) {
        questDTO.setRequiredScheduleNum(1);// 目前添加任务时默认设置完成任务所需进度为1
        // 检查任务的参数
        this.checkQuestParams(questDTO);
        // 添加任务和任务信息
        QuestInfo questInfo = QuestUtil.createQuestInfo(questDTO);
        questInfoMapper.insert(questInfo);
        Quest quest = QuestUtil.createQuest(questDTO);
        quest.setInfoId(questInfo.getId());
        Long preQuestId = questDTO.getPreQuestId();
        if (preQuestId == null) {
            // 如果没有前置任务，它就是根结点
            quest.setLeftValue(1);
            quest.setRightValue(2);
        } else {
            // 如果有前置任务，更新左右值
            Quest preQuest = questMapper.selectById(preQuestId);
            Integer preLeftValue = preQuest.getLeftValue();
            Integer preRightValue = preQuest.getRightValue();
            questMapper.addUpdateLeftValue(preLeftValue, preRightValue);
            questMapper.addUpdateRightValue(preRightValue);
            quest.setLeftValue(preRightValue);
            quest.setRightValue(preRightValue + 1);
        }
        questMapper.insert(quest);
        if (quest.getId() == null) {
            throw new BusinessException(SERVER_ERROR, "任务添加失败，请稍后重试");
        }
        // 检查进度的参数
        QuestUtil.checkScheduleParams(questDTO);
        // 添加进度
        QuestSchedule questSchedule = QuestUtil.createQuestSchedule(questDTO);
        // 地点有关信息，根据表单提供的是否需要地点的字段来设置
        if (questDTO.getNeedLocation() == 1) {
            // 检查地点参数
            QuestUtil.checkLocationParams(questDTO);
            // 添加地点
            QuestLocation questLocation = QuestUtil.createQuestLocation(questDTO);
            questLocationMapper.insert(questLocation);
            Long locationId = questLocation.getId();
            if (locationId == null) {
                throw new BusinessException(SERVER_ERROR, "任务添加失败，请稍后重试");
            }
            questSchedule.setLocationId(locationId);// 进度地点ID
            List<String> urls = questDTO.getLocationPhotoUrls();
            questLocationPhotoService.addBatchByUrls(urls, locationId);
        }
        questSchedule.setQuestId(quest.getId());// 所属任务ID
        questScheduleMapper.insert(questSchedule);
        if (questSchedule.getId() == null) {
            throw new BusinessException(SERVER_ERROR, "任务添加失败，请稍后重试");
        }
        return quest.getId();
    }

    @Transactional
    @Override
    public Boolean update(QuestDTO questUpdateDTO) {
        // TODO 为了整体的一致性，不允许修改前置任务
        this.checkQuestParams(questUpdateDTO);
        Quest quest = new Quest();
        BeanUtil.copyProperties(questUpdateDTO, quest);
        quest.setId(questUpdateDTO.getQuestId());
        LocalDateTime updateTime = LocalDateTime.now();
        quest.setUpdateTime(updateTime);
        // TODO 最大通过需求数量不能超过进度总数
        // TODO 支线下面不能有主线
        if (questMapper.updateById(quest) == 0) {
            return false;
        }
        Long infoId = questMapper.selectById(questUpdateDTO.getQuestId()).getInfoId();
        QuestInfo questInfo = new QuestInfo();
        BeanUtil.copyProperties(questUpdateDTO, questInfo);
        questInfo.setId(infoId);
        questInfo.setUpdateTime(updateTime);
        return questInfoMapper.updateById(questInfo) > 0;
    }

    @Transactional
    @Override
    public Boolean deleteById(Long id) {
        // 查询这个任务还有没有子任务
        Quest quest = questMapper.selectById(id);
        if (quest == null) return false;
        Integer leftValue = quest.getLeftValue();
        Integer rightValue = quest.getRightValue();
        if (rightValue - leftValue > 1) {// 只需要判断左右值之差是否为1
            throw new BusinessException(FORBIDDEN, "删除失败，该任务还存在后置任务");
        }
        questScheduleService.deleteByQuestId(id);
        // 删除任务详情
        questInfoMapper.deleteById(quest.getInfoId());
        int rows = questMapper.deleteById(id);
        // 修改左右值
        questMapper.deleteUpdateLeftValue(leftValue);
        questMapper.deleteUpdateRightValue(rightValue);
        return rows > 0;
    }
}
