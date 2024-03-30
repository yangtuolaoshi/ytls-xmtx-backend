package love.ytlsnb.quest.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.utils.UserHolder;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.quest.dto.QuestDTO;
import love.ytlsnb.model.quest.dto.QuestQueryDTO;
import love.ytlsnb.model.quest.po.*;
import love.ytlsnb.model.quest.vo.QuestInfoPageVO;
import love.ytlsnb.model.quest.vo.QuestInfoVo;
import love.ytlsnb.model.quest.vo.QuestListItemVO;
import love.ytlsnb.model.quest.vo.QuestVo;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.quest.mapper.*;
import love.ytlsnb.quest.service.QuestLocationPhotoService;
import love.ytlsnb.quest.service.QuestScheduleClockInMethodService;
import love.ytlsnb.quest.service.QuestScheduleService;
import love.ytlsnb.quest.service.QuestService;
import love.ytlsnb.quest.utils.QuestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
    private QuestLogMapper questLogMapper;

    @Autowired
    private QuestScheduleService questScheduleService;

    @Autowired
    private QuestScheduleClockInMethodService questScheduleClockInMethodService;

    @Autowired
    private QuestLocationPhotoService questLocationPhotoService;

    @Override
    public PageResult<List<QuestVo>> getPageByCondition(QuestQueryDTO questQueryDTO, int page, int size) {
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
        Long schoolId = questQueryDTO.getSchoolId();
        if (schoolId == null) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "参数非法");
        }
        queryWrapper.eq(Quest::getSchoolId, schoolId);
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
    public List<Quest> getAll(Long schoolId) {
        LambdaQueryWrapper<Quest> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Quest::getId, Quest::getQuestTitle)
                .eq(Quest::getSchoolId, schoolId);
        return questMapper.selectList(queryWrapper);
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
        QuestInfoVo questInfoVo = new QuestInfoVo();
        // 查询这个任务的总进度数
        Long scheduleNum = questScheduleService.getCountByQuestId(id);
        questInfoVo.setScheduleNum(scheduleNum);
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

    @Transactional
    @Override
    public Long add(QuestDTO questDTO) {
        Long schoolId = questDTO.getSchoolId();
        questDTO.setRequiredScheduleNum(1);// 目前添加任务时默认设置完成任务所需进度为1
        // 检查任务的参数
        QuestUtil.checkQuestParams(questDTO);
        // 检查前置任务存不存在
        Long preQuestId = questDTO.getPreQuestId();
        if (preQuestId != null) {
            LambdaQueryWrapper<Quest> preQuestQueryWrapper = new LambdaQueryWrapper<>();
            preQuestQueryWrapper.eq(Quest::getId, preQuestId);
            if (questMapper.selectOne(preQuestQueryWrapper) == null) {
                throw new BusinessException(UNPROCESSABLE_ENTITY, "前置任务不存在");
            }
        }
        // 添加任务和任务信息
        QuestInfo questInfo = QuestUtil.createQuestInfo(questDTO);
        questInfoMapper.insert(questInfo);
        Quest quest = QuestUtil.createQuest(questDTO);
        quest.setInfoId(questInfo.getId());
        if (preQuestId == null) {
            // 如果没有前置任务，它就是根结点
            quest.setLeftValue(1);
            quest.setRightValue(2);
            Integer maxTreeId = questMapper.getMaxTreeId(schoolId);
            if (maxTreeId == null) {// 如果是null，那么就是第一个添加的任务
                quest.setTreeId(1);
            } else {// 如果不是null，说明还有其它树
                quest.setTreeId(maxTreeId + 1);
            }
        } else {
            // 如果有前置任务，更新左右值
            Quest preQuest = questMapper.selectById(preQuestId);
            Integer preLeftValue = preQuest.getLeftValue();
            Integer preRightValue = preQuest.getRightValue();
            Integer treeId = preQuest.getTreeId();
            questMapper.addUpdateLeftValue(preLeftValue, preRightValue, schoolId, treeId);
            questMapper.addUpdateRightValue(preRightValue, schoolId, treeId);
            quest.setLeftValue(preRightValue);
            quest.setRightValue(preRightValue + 1);
            quest.setTreeId(treeId);
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
        // TODO 如果设置不启用地点，那么就不能选择地点打卡方式
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
        Long questScheduleId = questSchedule.getId();
        if (questScheduleId == null) {
            throw new BusinessException(SERVER_ERROR, "任务添加失败，请稍后重试");
        }
        // 进度添加打卡方式
        List<Long> clockMethodIds = questDTO.getClockMethodIds();
        questScheduleClockInMethodService.addClockInMethod2Schedule(questScheduleId, clockMethodIds);
        return quest.getId();
    }

    @Transactional
    @Override
    public Boolean update(QuestDTO questUpdateDTO) {
        // TODO 为了整体的一致性，不允许修改前置任务
        QuestUtil.checkQuestParams(questUpdateDTO);
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
    public Boolean deleteById(Long id, Long schoolId) {
        // 查询这个任务还有没有子任务
        Quest quest = questMapper.selectById(id);
        if (quest == null) {
            return false;
        }
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
        Integer treeId = quest.getTreeId();
        questMapper.deleteUpdateLeftValue(leftValue, schoolId, treeId);
        questMapper.deleteUpdateRightValue(rightValue, schoolId, treeId);
        return rows > 0;
    }

    @Override
    public QuestInfoPageVO getQuestInfoById(Long id) {
        // 任务信息
        QuestInfoPageVO questInfoPageVO = new QuestInfoPageVO();
        Quest quest = questMapper.selectById(id);
        if (quest == null) {
            throw new BusinessException(NOT_FOUND, "任务不存在");
        }
        Long questId = quest.getId();
        questInfoPageVO.setQuestId(questId);
        BeanUtil.copyProperties(quest, questInfoPageVO);
        QuestInfo questInfo = questInfoMapper.selectById(quest.getInfoId());
        BeanUtil.copyProperties(questInfo, questInfoPageVO);
        // 查询总进度数
        Long scheduleCount = questScheduleService.getCountByQuestId(questId);
        questInfoPageVO.setScheduleNum(scheduleCount);
        // 查询任务是否已完成
        LambdaQueryWrapper<QuestLog> queryWrapper = new LambdaQueryWrapper<>();
        Long userId = UserHolder.getUser().getId();
        queryWrapper
                .eq(QuestLog::getQuestId, questId)
                .eq(QuestLog::getUserId, userId);
        QuestLog questLog = questLogMapper.selectOne(queryWrapper);
        if (questLog == null) {
            questInfoPageVO.setIsFinished(0);
        } else {
            questInfoPageVO.setIsFinished(1);
        }
        return questInfoPageVO;
    }

    @Override
    public List<QuestListItemVO> getOngoingQuests(Integer type) {
        List<QuestListItemVO> questListItemVOS = new LinkedList<>();
        // 查询已完成的任务-所有叶子结点（左右值差为1的结点）
        User user = UserHolder.getUser();
        Long userId = user.getId();
        Long schoolId = user.getSchoolId();
        LambdaQueryWrapper<QuestLog> questLogQueryWrapper = new LambdaQueryWrapper<>();
        questLogQueryWrapper
                .eq(QuestLog::getUserId, userId);
        List<QuestLog> questLogs = questLogMapper.selectList(questLogQueryWrapper);
        // 转哈希表，方便做差集
        Set<Quest> finishedQuestSet = new HashSet<>();
        questLogs.forEach(questLog -> {
            Quest quest = new Quest();
            quest.setId(questLog.getQuestId());
            finishedQuestSet.add(quest);
        });
        LambdaQueryWrapper<Quest> questQueryWrapper = new LambdaQueryWrapper<>();
        if (questLogs.size() == 0) {
            // 如果没有任务完成情况，就返回所有的根任务
            questQueryWrapper
                    .isNull(Quest::getParentId)
                    .eq(Quest::getSchoolId, schoolId);
        } else {
            // 如果有任务完成
            // 1. 查询所有已完成任务的子任务
            // 先构造SQL
            StringBuilder sb = new StringBuilder();
            sb.append("(parent_id in (");
            for (int i = 0; i < questLogs.size(); i++) {
                QuestLog questLog = questLogs.get(i);
                Long preQuestId = questLog.getQuestId();
                if (i < questLogs.size() - 1) {
                    sb.append(preQuestId).append(",");
                } else {
                    sb.append(preQuestId);
                }
            }
            sb.append(") or parent_id is null)");
            String inSQL = sb.toString();
            questQueryWrapper
                    .apply(inSQL)
                    .eq(Quest::getSchoolId, schoolId);
        }
        if (type != null) {// 类型查询
            questQueryWrapper.eq(Quest::getType, type);
        }
        List<Quest> quests = questMapper.selectList(questQueryWrapper);
        Set<Quest> questSet = new HashSet<>(quests);
        // 过滤已完成的任务，利用哈希表做差集
        questSet.removeAll(finishedQuestSet);
        questSet.forEach(quest -> {
            QuestListItemVO questListItemVO = new QuestListItemVO();
            BeanUtil.copyProperties(quest, questListItemVO);
            questListItemVOS.add(questListItemVO);
        });
        return questListItemVOS;
    }

    @Override
    public List<QuestListItemVO> getFinishedQuests(Integer type) {
        User user = UserHolder.getUser();
        Long userId = user.getId();
        // 查询用户已完成的任务记录
        LambdaQueryWrapper<QuestLog> questLogQueryWrapper = new LambdaQueryWrapper<>();
        questLogQueryWrapper.eq(QuestLog::getUserId, userId);
        List<QuestLog> questLogs = questLogMapper.selectList(questLogQueryWrapper);
        LinkedList<Long> questIds = new LinkedList<>();
        questLogs.forEach(questLog -> {
            Long questId = questLog.getQuestId();
            questIds.add(questId);
        });
        // 转为任务
        LambdaQueryWrapper<Quest> questQueryWrapper = new LambdaQueryWrapper<>();
        questQueryWrapper.in(Quest::getId, questIds);
        if (type != null) {
            questQueryWrapper.eq(Quest::getType, type);
        }
        List<Quest> quests = questMapper.selectList(questQueryWrapper);
        List<QuestListItemVO> questListItemVOS = new LinkedList<>();
        quests.forEach(quest -> {
            QuestListItemVO questListItemVO = new QuestListItemVO();
            Long questId = quest.getId();
            questListItemVO.setId(questId);
            BeanUtil.copyProperties(quest, questListItemVO);
            questListItemVOS.add(questListItemVO);
        });
        return questListItemVOS;
    }

    @Override
    public List<QuestListItemVO> getUnstartedQuests(Integer type) {
        return null;
    }
}
