package love.ytlsnb.quest.service.impl;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.quest.dto.LocationCheckDTO;
import love.ytlsnb.quest.mapper.ClockInLogMapper;
import love.ytlsnb.quest.service.ClockInLogService;
import love.ytlsnb.quest.service.QuestScheduleLogService;
import love.ytlsnb.quest.template.ClockInTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 打卡记录业务层实现类
 *
 * @author 金泓宇
 * @date 2024/3/15
 */
@Slf4j
@Service
public class ClockInLogServiceImpl implements ClockInLogService {
    @Autowired
    private ClockInLogMapper clockInLogMapper;

    @Autowired
    private QuestScheduleLogService questScheduleLogService;

    private final ClockInTemplate<LocationCheckDTO> locationClockIn;

    private final ClockInTemplate<MultipartFile> photoClockIn;

    private final ClockInTemplate<MultipartFile> faceClockIn;

    private final ClockInTemplate adminClockIn;

    @Autowired
    public ClockInLogServiceImpl(
            @Qualifier("locationClockIn") ClockInTemplate<LocationCheckDTO> locationClockIn,
            @Qualifier("photoClockIn") ClockInTemplate<MultipartFile> photoClockIn,
            @Qualifier("faceClockIn") ClockInTemplate<MultipartFile> faceClockIn,
            @Qualifier("adminClockIn") ClockInTemplate adminCheckIn) {
        this.locationClockIn = locationClockIn;
        this.photoClockIn = photoClockIn;
        this.faceClockIn = faceClockIn;
        this.adminClockIn = adminCheckIn;
    }

    @Transactional
    @Override
    public Boolean locationCheck(LocationCheckDTO locationCheckDTO, Long questScheduleId) {
        return locationClockIn.clockIn(questScheduleId, locationCheckDTO);
    }

    @Override
    public Boolean photoCheck(Long questScheduleId, MultipartFile photo) {
        return photoClockIn.clockIn(questScheduleId, photo);
    }

    @Override
    public Boolean faceCheck(Long questScheduleId) {
        return faceClockIn.clockIn(questScheduleId, null);
    }

    @Override
    public Boolean adminCheck(Long questScheduleId) {
        return adminClockIn.clockIn(questScheduleId, null);
    }
}
