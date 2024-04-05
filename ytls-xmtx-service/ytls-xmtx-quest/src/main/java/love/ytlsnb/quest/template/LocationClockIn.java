package love.ytlsnb.quest.template;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.model.quest.dto.LocationCheckDTO;
import love.ytlsnb.model.quest.po.QuestLocation;
import love.ytlsnb.model.quest.po.QuestSchedule;
import love.ytlsnb.quest.mapper.ClockInLogMapper;
import love.ytlsnb.quest.mapper.QuestLocationMapper;
import love.ytlsnb.quest.mapper.QuestScheduleMapper;
import love.ytlsnb.quest.service.QuestScheduleLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static love.ytlsnb.common.constants.ResultCodes.UNPROCESSABLE_ENTITY;

/**
 * 地点打卡实现类
 *
 * @author 金泓宇
 * @date 2024/3/15
 */
@Component("locationClockIn")
@Slf4j
public class LocationClockIn extends ClockInTemplate<LocationCheckDTO> {
    // 地球平均半径，单位：米
    private static final double EARTH_RADIUS = 6371000;

    @Autowired
    private QuestScheduleMapper questScheduleMapper;

    @Autowired
    private QuestLocationMapper questLocationMapper;

    @Autowired
    public LocationClockIn(ClockInLogMapper clockInLogMapper, QuestScheduleLogService questScheduleLogService) {
        super(clockInLogMapper, questScheduleLogService, 1L);
    }

    // 将角度转换为弧度
    private double toRadians(double degree) {
        return degree * Math.PI / 180.0;
    }

    // 计算两个经纬度之间的距离，返回值单位为米
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = toRadians(lat1);
        double lat2Rad = toRadians(lat2);
        double deltaLat = toRadians(lat2 - lat1);
        double deltaLon = toRadians(lon2 - lon1);
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    @Override
    protected boolean clockInTest(Long questScheduleId, LocationCheckDTO locationCheckDTO) {
        // 检测参数
        if (locationCheckDTO == null) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "打卡参数错误");
        }
        Double longitude = locationCheckDTO.getLongitude();
        Double latitude = locationCheckDTO.getLatitude();
        if (longitude == null || latitude == null) {
            throw new BusinessException(UNPROCESSABLE_ENTITY, "打卡参数错误");
        }
        // 获取打卡地点
        QuestSchedule questSchedule = questScheduleMapper.selectById(questScheduleId);
        Long locationId = questSchedule.getLocationId();
        QuestLocation questLocation = questLocationMapper.selectById(locationId);
        Double targetLatitude = questLocation.getLatitude();
        Double targetLongitude = questLocation.getLongitude();
        // 计算距离
        double distance = calculateDistance(targetLatitude, targetLongitude, latitude, longitude);
        log.info("目标位置：({},{})；实际位置：({},{})；距离：({})",
                targetLongitude,
                targetLatitude,
                longitude,
                latitude,
                distance
        );
        // TODO 这个距离以后应该做成能在管理平台设置的
        return distance < 100;
    }
}
