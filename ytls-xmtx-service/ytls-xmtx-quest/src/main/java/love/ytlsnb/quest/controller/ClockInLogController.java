package love.ytlsnb.quest.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.quest.dto.LocationCheckDTO;
import love.ytlsnb.quest.service.ClockInLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 打卡记录表现层，主要提供打卡的方法
 *
 * @author 金泓宇
 * @date 2024/3/15
 */
@RestController
@RequestMapping("/clockIn")
@Slf4j
public class ClockInLogController {
    @Autowired
    private ClockInLogService clockInLogService;

    /**
     * 地点打卡
     * @param locationCheckDTO 打卡条件-进度名称、经纬度
     * @param questScheduleId 进度ID
     * @return 是否成功
     */
    @PostMapping("/location/{questScheduleId}")
    public Result<Boolean> locationCheck(@RequestBody LocationCheckDTO locationCheckDTO, @PathVariable Long questScheduleId) {
        return Result.ok(clockInLogService.locationCheck(locationCheckDTO, questScheduleId));
    }

    /**
     * 照片打卡
     * @param questScheduleId 进度ID
     * @return 是否成功
     */
    @PostMapping("/photo/{questScheduleId}")
    public Result<Boolean> photoCheck(@PathVariable Long questScheduleId, MultipartFile photo) {
        return Result.ok(clockInLogService.photoCheck(questScheduleId, photo));
    }

    /**
     * 人脸打卡
     * @param questScheduleId 进度ID
     * @return 是否成功
     */
    @PostMapping("/face/{questScheduleId}")
    public Result<Boolean> faceCheck(@PathVariable Long questScheduleId) {
        return Result.ok(clockInLogService.faceCheck(questScheduleId));
    }

    /**
     * 管理员打卡
     * @param questScheduleId 进度ID
     * @return 是否成功
     */
    @PostMapping("/admin/{questScheduleId}")
    public Result<Boolean> adminCheck(@PathVariable Long questScheduleId) {
        return Result.ok(clockInLogService.adminCheck(questScheduleId));
    }
}
