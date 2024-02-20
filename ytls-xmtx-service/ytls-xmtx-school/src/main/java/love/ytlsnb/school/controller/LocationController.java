package love.ytlsnb.school.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.school.dto.LocationInsertDTO;
import love.ytlsnb.model.school.vo.LocationVO;
import love.ytlsnb.school.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ula
 * @date 2024/2/20 8:33
 */
@Slf4j
@RestController
@RequestMapping("location")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @PostMapping("location")
    public Result addLocation(@RequestBody LocationInsertDTO locationInsertDTO) {
        log.info("新增学校建筑");
        // TODO 新增学校建筑逻辑
        return Result.ok();
    }

    @GetMapping("/{locationId}")
    public Result<LocationVO> getWholeLocationById(@PathVariable Long locationId) {
        log.info("查询学校地点:{}", locationId);
        return Result.ok(locationService.getWholeLocationById(locationId));
    }

}
