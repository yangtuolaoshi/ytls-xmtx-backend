package love.ytlsnb.quest.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.quest.dto.MapFilterDTO;
import love.ytlsnb.model.quest.dto.QuestDTO;
import love.ytlsnb.model.quest.po.Quest;
import love.ytlsnb.model.quest.po.QuestLocation;
import love.ytlsnb.model.quest.po.QuestLocationPhoto;
import love.ytlsnb.model.quest.po.QuestSchedule;
import love.ytlsnb.model.quest.vo.QuestScheduleInfoVO;
import love.ytlsnb.model.quest.vo.QuestScheduleVo;
import love.ytlsnb.quest.mapper.QuestLocationMapper;
import love.ytlsnb.quest.mapper.QuestLocationPhotoMapper;
import love.ytlsnb.quest.mapper.QuestMapper;
import love.ytlsnb.quest.mapper.QuestScheduleMapper;
import love.ytlsnb.quest.service.QuestLocationPhotoService;
import love.ytlsnb.quest.service.QuestScheduleService;
import love.ytlsnb.quest.utils.QuestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

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
    private QuestLocationPhotoMapper questLocationPhotoMapper;

    @Autowired
    private QuestLocationPhotoService questLocationPhotoService;

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
        LambdaQueryWrapper<QuestSchedule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuestSchedule::getScheduleTitle, questDTO.getQuestTitle())
                .eq(QuestSchedule::getQuestId, questDTO.getQuestId());
        if (questScheduleMapper.selectOne(queryWrapper) != null) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "标题重复了，换一个试试");
        }
        questSchedule.setQuestId(questDTO.getQuestId());
        questScheduleMapper.insert(questSchedule);
        return questSchedule.getId();
    }

    @Override
    public QuestScheduleInfoVO getInfoById(Long id) {
        QuestScheduleInfoVO questScheduleInfoVO = new QuestScheduleInfoVO();
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
        LambdaQueryWrapper<QuestSchedule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuestSchedule::getQuestId, questSchedule.getQuestId());
        List<QuestSchedule> questSchedules = questScheduleMapper.selectList(queryWrapper);
        if (questSchedules.size() < 2) {
            throw new BusinessException(FORBIDDEN, "请至少保留一个进度");
        }
        questLocationMapper.deleteById(questSchedule.getLocationId());
        return questScheduleMapper.deleteById(id) > 0;
    }

    @Override
    public Boolean deleteByQuestId(Long questId) {
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
        if (questLocationIds.size() > 0) {
            questLocationMapper.deleteBatchIds(questLocationIds);
        }
        return questScheduleMapper.deleteBatchIds(questScheduleIds) > 0;
    }

    @Override
    public Long getCountByQuestId(Long questId) {
        LambdaQueryWrapper<QuestSchedule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuestSchedule::getQuestId, questId);
        return questScheduleMapper.selectCount(queryWrapper);
    }
}
