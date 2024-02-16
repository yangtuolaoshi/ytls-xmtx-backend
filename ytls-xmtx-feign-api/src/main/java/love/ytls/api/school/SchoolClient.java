package love.ytls.api.school;

import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.school.po.School;
import love.ytlsnb.model.school.vo.LocationVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author ula
 * @date 2024/2/3 15:13
 */
@FeignClient("school-service")
public interface SchoolClient {
    @GetMapping("/api/school/list")
    Result<List<School>> list();

    @GetMapping("/api/school/location/{locationId}")
    public Result<LocationVO> getWholeLocationById(@PathVariable Long locationId);
}
