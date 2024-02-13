package love.ytlsnb.school.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.ytlsnb.model.school.po.Location;
import love.ytlsnb.model.school.vo.LocationVO;

/**
 * @author ula
 * @date 2024/2/5 9:38
 */
public interface LocationService extends IService<Location> {
    LocationVO getWholeLocationById(Long locationId);
}
