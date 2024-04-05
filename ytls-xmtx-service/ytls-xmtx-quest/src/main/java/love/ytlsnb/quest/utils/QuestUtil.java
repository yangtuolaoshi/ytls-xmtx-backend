package love.ytlsnb.quest.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.model.quest.dto.QuestDTO;
import love.ytlsnb.model.quest.po.Quest;
import love.ytlsnb.model.quest.po.QuestInfo;
import love.ytlsnb.model.quest.po.QuestLocation;
import love.ytlsnb.model.quest.po.QuestSchedule;

import java.time.LocalDateTime;
import java.util.List;

import static love.ytlsnb.common.constants.ResultCodes.UNPROCESSABLE_ENTITY;

/**
 * 任务和进度数据参数检测和创建对象工具类
 * @author 金泓宇
 * @date 2024/3/4
 */
public final class QuestUtil {
    private QuestUtil() {}

    /**
     * 任务参数检测
     *
     * @param questDTO 任务添加表单
     */
    public static void checkQuestParams(QuestDTO questDTO) {
        // 标题
        String questTitle = questDTO.getQuestTitle();
        if (questTitle == null || "".equals(questTitle)) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "请输入任务标题");
        }
        if (questTitle.length() > 16) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "任务标题不能超过16个字符");
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

    /**
     * 进度参数检测
     * @param questDTO 任务添加表单
     */
    public static void checkScheduleParams(QuestDTO questDTO) {
        String scheduleTitle = questDTO.getScheduleTitle();
        if (scheduleTitle == null || "".equals(scheduleTitle)) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "请输入进度标题");
        }
        if (scheduleTitle.length() > 16) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "进度标题不能超过16个字符");
        }
        // 打卡方式
        List<Long> clockMethodIds = questDTO.getClockMethodIds();
        if (clockMethodIds == null || clockMethodIds.size() == 0) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "请至少选择一种打卡方式");
        }
        // 进度启用状态
        Integer scheduleStatus = questDTO.getScheduleStatus();
        if (scheduleStatus == null || scheduleStatus > 1 || scheduleStatus < 0) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "请设置进度状态");
        }
    }

    /**
     * 地点参数检测
     * @param questDTO 任务添加表单
     */
    public static void checkLocationParams(QuestDTO questDTO) {
        if (questDTO.getNeedLocation() == 1) {
            String locationName = questDTO.getLocationName();
            if (locationName == null || "".equals(locationName)) {
                throw new BusinessException(UNPROCESSABLE_ENTITY, "请输入地点名称");
            }
            String locationDescription = questDTO.getLocationDescription();
            if (locationDescription != null && locationDescription.length() > 256) {
                throw new BusinessException(UNPROCESSABLE_ENTITY, "地点描述不能超过256个字符");
            }
            Double longitude = questDTO.getLongitude();
            if (longitude == null || longitude > 180 || longitude < 0) {
                throw new BusinessException(UNPROCESSABLE_ENTITY, "经度非法");
            }
            Double latitude = questDTO.getLatitude();
            if (latitude == null || latitude > 90 || latitude < 0) {
                throw new BusinessException(UNPROCESSABLE_ENTITY, "纬度非法");
            }
        }
    }

    // TODO 创建对象的操作在修改表结构后可以使用BeanUtil简化

    /**
     * 创建任务对象
     * @param questDTO 任务添加表单
     * @return 任务对象
     */
    public static Quest createQuest(QuestDTO questDTO) {
        Quest quest = new Quest();
        quest.setType(questDTO.getType());
        quest.setQuestTitle(questDTO.getQuestTitle());
        quest.setReward(questDTO.getReward());
        quest.setRequiredScheduleNum(questDTO.getRequiredScheduleNum());
        quest.setQuestStatus(questDTO.getQuestStatus());
        quest.setParentId(questDTO.getPreQuestId());
        quest.setEndTime(questDTO.getEndTime());
        quest.setSchoolId(questDTO.getSchoolId());
        quest.setCreateTime(LocalDateTime.now());
        quest.setIsDeleted(0);
        return quest;
    }

    /**
     * 创建任务详情对象
     * @param questDTO 任务添加表单
     * @return 任务详情对象
     */
    public static QuestInfo createQuestInfo(QuestDTO questDTO) {
        QuestInfo questInfo = new QuestInfo();
        questInfo.setObjective(questDTO.getObjective());
        questInfo.setQuestDescription(questDTO.getQuestDescription());
        questInfo.setTip(questDTO.getTip());
        questInfo.setRequiredItem(questDTO.getRequiredItem());
        questInfo.setCreateTime(LocalDateTime.now());
        questInfo.setIsDeleted(0);
        return questInfo;
    }

    /**
     * 创建进度对象
     * @param questDTO 任务添加表单
     * @return 进度对象
     */
    public static QuestSchedule createQuestSchedule(QuestDTO questDTO) {
        QuestSchedule questSchedule = new QuestSchedule();
        questSchedule.setScheduleTitle(questDTO.getScheduleTitle());
        questSchedule.setScheduleStatus(questDTO.getScheduleStatus());
        questSchedule.setCreateTime(LocalDateTime.now());
        questSchedule.setNeedLocation(questDTO.getNeedLocation());
        // TODO 设置打卡方式
        questSchedule.setIsDeleted(0);
        return questSchedule;
    }

    /**
     * 创建任务地点对象
     * @param questDTO 任务添加表单
     * @return 任务地点对象
     */
    public static QuestLocation createQuestLocation(QuestDTO questDTO) {
        QuestLocation questLocation = new QuestLocation();
        questLocation.setLocationName(questDTO.getLocationName());
        questLocation.setLocationDescription(questDTO.getLocationDescription());
        questLocation.setLongitude(questDTO.getLongitude());
        questLocation.setLatitude(questDTO.getLatitude());
        questLocation.setCreateTime(LocalDateTime.now());
        questLocation.setIsDeleted(0);
        return questLocation;
    }
}
