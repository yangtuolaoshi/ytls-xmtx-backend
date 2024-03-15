package love.ytlsnb.quest.utils;

import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.model.quest.dto.QuestDTO;
import love.ytlsnb.model.quest.po.Quest;
import love.ytlsnb.model.quest.po.QuestInfo;
import love.ytlsnb.model.quest.po.QuestLocation;
import love.ytlsnb.model.quest.po.QuestSchedule;

import java.time.LocalDateTime;

import static love.ytlsnb.common.constants.ResultCodes.UNPROCESSABLE_ENTITY;

/**
 * 任务和进度数据参数检测和创建对象工具类
 * @author 金泓宇
 * @date 2024/3/4
 */
public final class QuestUtil {
    private QuestUtil() {}

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
        Integer adminCheck = questDTO.getAdminCheck();
        Integer photoCheck = questDTO.getPhotoCheck();
        Integer locationCheck = questDTO.getLocationCheck();
        Integer faceCheck = questDTO.getFaceCheck();
        if (adminCheck == null && photoCheck == null && locationCheck == null && faceCheck == null) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "请至少选择一种打卡方式");
        }
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
        questSchedule.setAdminCheck(questDTO.getAdminCheck());
        questSchedule.setPhotoCheck(questDTO.getPhotoCheck());
        questSchedule.setLocationCheck(questDTO.getLocationCheck());
        questSchedule.setFaceCheck(questDTO.getFaceCheck());
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
