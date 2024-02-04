package love.ytls.api.school;

import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.school.po.School;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author ula
 * @date 2024/2/3 15:13
 */
@FeignClient("school-service")
public interface SchoolClient {
    @GetMapping("list")
    Result<List<School>> list();
}
