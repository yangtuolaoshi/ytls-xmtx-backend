package love.ytlsnb.quest.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.utils.UserHolder;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.quest.dto.MapFilterDTO;
import love.ytlsnb.model.quest.dto.QuestDTO;
import love.ytlsnb.model.quest.po.*;
import love.ytlsnb.model.quest.vo.*;
import love.ytlsnb.quest.mapper.*;
import love.ytlsnb.quest.service.ClockInMethodService;
import love.ytlsnb.quest.service.QuestLocationPhotoService;
import love.ytlsnb.quest.service.QuestScheduleClockInMethodService;
import love.ytlsnb.quest.service.QuestScheduleService;
import love.ytlsnb.quest.utils.QuestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static love.ytlsnb.common.constants.ResultCodes.*;

/**
 * 任务进度业务层实现类
 *
 * @author 金泓宇
 * @date 2024/3/4
 */
@Service
public class QuestScheduleServiceImpl implements QuestScheduleService {
    @Autowired
    private QuestMapper questMapper;

    @Autowired
    private QuestScheduleMapper questScheduleMapper;

    @Autowired
    private QuestLocationMapper questLocationMapper;

    @Autowired
    private QuestScheduleLogMapper questScheduleLogMapper;

    @Autowired
    private QuestLocationPhotoMapper questLocationPhotoMapper;

    @Autowired
    private ClockInLogMapper clockInLogMapper;

    @Autowired
    private QuestLocationPhotoService questLocationPhotoService;

    @Autowired
    private ClockInMethodService clockInMethodService;

    @Autowired
    private QuestScheduleClockInMethodService questScheduleClockInMethodService;

    /**
     * 检查任务表单数据
     *
     * @param questDTO 表单数据封装
     */
    private void checkQuestParams(QuestDTO questDTO) {
        Long questId = questDTO.getQuestId();
        if (questId == null) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "必须选择所属任务");
        }
        Quest quest = questMapper.selectById(questId);
        if (quest == null) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "所选任务不存在");
        }
    }

    @Override
    public List<QuestScheduleVo> getAdminMap(MapFilterDTO mapFilterDTO, Long schoolId) {
        return questScheduleMapper.getAdminMap(mapFilterDTO, schoolId);
    }

    @Transactional
    @Override
    public Long add(QuestDTO questDTO) {
        this.checkQuestParams(questDTO);
        // 添加地点
        QuestUtil.checkLocationParams(questDTO);
        QuestUtil.checkScheduleParams(questDTO);
        QuestSchedule questSchedule = QuestUtil.createQuestSchedule(questDTO);
        if (questDTO.getNeedLocation() == 1) {
            QuestLocation questLocation = QuestUtil.createQuestLocation(questDTO);
            questLocationMapper.insert(questLocation);
            Long locationId = questLocation.getId();
            if (locationId == null) {
                throw new BusinessException(SERVER_ERROR, "进度添加失败，请稍后重试");
            }
            questSchedule.setLocationId(locationId);
            List<String> photoUrls = questDTO.getLocationPhotoUrls();
            questLocationPhotoService.addBatchByUrls(photoUrls, locationId);
        }
        // 添加进度
//        LambdaQueryWrapper<QuestSchedule> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(QuestSchedule::getScheduleTitle, questDTO.getQuestTitle())
//                .eq(QuestSchedule::getQuestId, questDTO.getQuestId());
//        if (questScheduleMapper.selectOne(queryWrapper) != null) {
//            throw new BusinessException(UNPROCESSABLE_ENTITY, "标题重复了，换一个试试");
//        }
        questSchedule.setQuestId(questDTO.getQuestId());
        questScheduleMapper.insert(questSchedule);
        // 添加打卡方式
        Long questScheduleId = questSchedule.getId();
        List<Long> clockMethodIds = questDTO.getClockMethodIds();
        questScheduleClockInMethodService.addClockInMethod2Schedule(questScheduleId, clockMethodIds);
        return questScheduleId;
    }

    @Override
    public QuestScheduleInfoVO getInfoById(Long id) {
        QuestScheduleInfoVO questScheduleInfoVO = new QuestScheduleInfoVO();
        // 查询进度基本信息
        QuestSchedule questSchedule = questScheduleMapper.selectById(id);
        if (questSchedule == null) {
            throw new BusinessException(NOT_FOUND, "进度不存在");
        }
        Quest quest = questMapper.selectById(questSchedule.getQuestId());
        if (quest == null) {
            throw new BusinessException(SERVER_ERROR, "数据异常，请联系管理员");
        }
        questScheduleInfoVO.setQuestTitle(quest.getQuestTitle());
        BeanUtil.copyProperties(questSchedule, questScheduleInfoVO);
        // 查询地点信息
        if (questSchedule.getNeedLocation() == 1 && questSchedule.getLocationId() != null) {
            QuestLocation questLocation = questLocationMapper.selectById(questSchedule.getLocationId());
            if (questLocation == null) {
                throw new BusinessException(SERVER_ERROR, "数据异常，请联系管理员");
            }
            BeanUtil.copyProperties(questLocation, questScheduleInfoVO);
            LambdaQueryWrapper<QuestLocationPhoto> photoQueryWrapper = new LambdaQueryWrapper<>();
            photoQueryWrapper.eq(QuestLocationPhoto::getLocationId, questLocation.getId());
            List<QuestLocationPhoto> photos = questLocationPhotoMapper.selectList(photoQueryWrapper);
            questScheduleInfoVO.setLocationPhotos(photos);
        }
        // 查询打卡方式
        List<ClockInMethod> clockInMethods = clockInMethodService.getByQuestScheduleId(id);
        questScheduleInfoVO.setClockInMethods(clockInMethods);
        return questScheduleInfoVO;
    }

    @Override
    public PageResult<List<QuestSchedule>> getPageByQuestId(Long questId, int page, int size) {
        if (questMapper.selectById(questId) == null) {
            throw new BusinessException(NOT_FOUND, "任务不存在");
        }
        LambdaQueryWrapper<QuestSchedule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuestSchedule::getQuestId, questId);
        Page<QuestSchedule> questSchedulePage = questScheduleMapper.selectPage(new Page<>(page, size), queryWrapper);
        List<QuestSchedule> records = questSchedulePage.getRecords();
        long total = questSchedulePage.getTotal();
        PageResult<List<QuestSchedule>> result = new PageResult<>(page, size, total);
        result.setData(records);
        return result;
    }

    @Transactional
    @Override
    public Boolean update(QuestDTO questDTO) {
        // 修改进度
        QuestSchedule questSchedule = new QuestSchedule();
        QuestUtil.checkScheduleParams(questDTO);
        QuestUtil.checkLocationParams(questDTO);
        BeanUtil.copyProperties(questDTO, questSchedule);
        questSchedule.setId(questDTO.getScheduleId());
        LocalDateTime updateTime = LocalDateTime.now();
        questSchedule.setUpdateTime(updateTime);
        questScheduleMapper.updateById(questSchedule);
        // 修改地点
        QuestLocation questLocation = new QuestLocation();
        Long locationId = questScheduleMapper.selectById(questSchedule.getId()).getLocationId();
        BeanUtil.copyProperties(questDTO, questLocation);
        questLocation.setId(locationId);
        questLocation.setUpdateTime(updateTime);
        questLocationMapper.updateById(questLocation);
        // TODO 图片更新
        return true;
    }

    @Transactional
    @Override
    public Boolean deleteById(Long id) {
        QuestSchedule questSchedule = questScheduleMapper.selectById(id);
        if (questSchedule == null) {
            return false;
        }
        // 至少保留一个进度
        LambdaQueryWrapper<QuestSchedule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuestSchedule::getQuestId, questSchedule.getQuestId());
        List<QuestSchedule> questSchedules = questScheduleMapper.selectList(queryWrapper);
        if (questSchedules.size() < 2) {
            throw new BusinessException(FORBIDDEN, "请至少保留一个进度");
        }
        // 删除地点信息
        Long locationId = questSchedule.getLocationId();
        questLocationMapper.deleteById(locationId);
        questLocationPhotoService.deleteByLocationId(locationId);
        // 删除打卡绑定
        questScheduleClockInMethodService.deleteByQuestScheduleId(id);
        return questScheduleMapper.deleteById(id) > 0;
    }

    @Transactional
    @Override
    public Boolean deleteByQuestId(Long questId) {
        // 找到所有的进度
        LambdaQueryWrapper<QuestSchedule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuestSchedule::getQuestId, questId);
        List<QuestSchedule> questSchedules = questScheduleMapper.selectList(queryWrapper);
        List<Long> questLocationIds = new LinkedList<>();
        List<Long> questScheduleIds = new LinkedList<>();
        questSchedules.forEach((questSchedule) -> {
            questScheduleIds.add(questSchedule.getId());
            if (questSchedule.getNeedLocation() == 1) {
                questLocationIds.add(questSchedule.getLocationId());
            }
        });
        // 删除所有地点信息
        if (questLocationIds.size() > 0) {
            questLocationMapper.deleteBatchIds(questLocationIds);
            // 删除所有地点图片信息
            questLocationPhotoService.deleteByLocationIds(questLocationIds);
        }
        // 删除所有打卡绑定
        questScheduleClockInMethodService.deleteByQuestScheduleIds(questScheduleIds);
        // 删除进度信息
        return questScheduleMapper.deleteBatchIds(questScheduleIds) > 0;
    }

    @Override
    public Long getCountByQuestId(Long questId) {
        LambdaQueryWrapper<QuestSchedule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuestSchedule::getQuestId, questId);
        return questScheduleMapper.selectCount(queryWrapper);
    }

    @Override
    public Collection<QuestScheduleCompletionVO> getQuestInfoPageSchedule(Long questId) {
        Long userId = UserHolder.getUser().getId();
//        return questScheduleMapper.getByQuestAndUserId(questId, userId);
        // 先查询该任务下的所有进度
        LambdaQueryWrapper<QuestSchedule> questScheduleQueryWrapper = new LambdaQueryWrapper<>();
        questScheduleQueryWrapper.eq(QuestSchedule::getQuestId, questId);
        List<QuestSchedule> questSchedules = questScheduleMapper.selectList(questScheduleQueryWrapper);
        Map<Long, QuestScheduleCompletionVO> questScheduleMap = new HashMap<>();// 哈希表便于查找
        List<Long> questScheduleIds = new LinkedList<>();
        questSchedules.forEach(questSchedule -> {
            Long questScheduleId = questSchedule.getId();
            String scheduleTitle = questSchedule.getScheduleTitle();
            // 暂存进度ID
            questScheduleIds.add(questScheduleId);
            // 创建完成情况对象
            QuestScheduleCompletionVO questScheduleCompletionVO = new QuestScheduleCompletionVO();
            questScheduleCompletionVO.setQuestId(questId);
            questScheduleCompletionVO.setScheduleTitle(scheduleTitle);
            questScheduleCompletionVO.setScheduleId(questScheduleId);
            questScheduleCompletionVO.setIsFinished(0);
            questScheduleMap.put(questScheduleId, questScheduleCompletionVO);
        });
        // 查询该用户的完成记录
        LambdaQueryWrapper<QuestScheduleLog> questScheduleLogQueryWrapper = new LambdaQueryWrapper<>();
        questScheduleLogQueryWrapper
                .eq(QuestScheduleLog::getUserId, userId)
                .in(QuestScheduleLog::getQuestScheduleId, questScheduleIds);
        List<QuestScheduleLog> questScheduleLogs = questScheduleLogMapper.selectList(questScheduleLogQueryWrapper);
        // 过滤已完成的进度
        questScheduleLogs.forEach(questScheduleLog -> {
            Long questScheduleId = questScheduleLog.getQuestScheduleId();
            QuestScheduleCompletionVO questScheduleCompletionVO = questScheduleMap.get(questScheduleId);
            // 不为null说明进度已完成了
            if (questScheduleCompletionVO != null) {
                questScheduleCompletionVO.setIsFinished(1);
            }
        });
        return questScheduleMap.values();
    }

    @Override
    public List<QuestScheduleMapPoint> getQuestInfoPageMap(Long questId) {
        Long userId = UserHolder.getUser().getId();
        return questScheduleMapper.getMapByQuestAndUserId(questId, userId);
    }

    @Override
    public QuestSchedulePageInfoVO getQuestScheduleInfoPage(Long id) {
        Long userId = UserHolder.getUser().getId();
        // 查询基本信息
        QuestSchedulePageInfoVO questSchedulePageInfoVO = new QuestSchedulePageInfoVO();
        QuestSchedule questSchedule = questScheduleMapper.selectById(id);
        BeanUtil.copyProperties(questSchedule, questSchedulePageInfoVO);
        Long questId = questSchedule.getQuestId();
        questSchedulePageInfoVO.setScheduleId(questId);
        // 查询是否完成
        LambdaQueryWrapper<QuestScheduleLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(QuestScheduleLog::getQuestScheduleId, id)
                .eq(QuestScheduleLog::getUserId, userId);
        QuestScheduleLog questScheduleLog = questScheduleLogMapper.selectOne(queryWrapper);
        if (questScheduleLog != null) {
            questSchedulePageInfoVO.setIsFinished(1);
        } else {
            questSchedulePageInfoVO.setIsFinished(0);
        }
        // 查询打卡记录
        LambdaQueryWrapper<ClockInLog> clockInLogQueryWrapper = new LambdaQueryWrapper<>();
        clockInLogQueryWrapper.eq(ClockInLog::getQuestScheduleId, id);
        List<ClockInLog> clockInLogs = clockInLogMapper.selectList(clockInLogQueryWrapper);
        questSchedulePageInfoVO.setClockInLogs(clockInLogs);
        // 查询这个进度所有的打卡方式
        List<ClockInMethod> clockInMethods = questScheduleClockInMethodService.getClockInMethodsByQuestScheduleId(id);
        questSchedulePageInfoVO.setClockInMethods(clockInMethods);
        return questSchedulePageInfoVO;
    }
}
