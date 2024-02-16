package love.ytlsnb.school.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.ytlsnb.common.constants.SchoolConstant;
import love.ytlsnb.model.school.po.Location;
import love.ytlsnb.model.school.po.LocationPhoto;
import love.ytlsnb.model.school.vo.LocationVO;
import love.ytlsnb.school.mapper.LocationMapper;
import love.ytlsnb.school.mapper.LocationPhotoMapper;
import love.ytlsnb.school.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ula
 * @date 2024/2/5 9:38
 */
@Service
public class LocationServiceImpl extends ServiceImpl<LocationMapper, Location> implements LocationService {
    @Autowired
    private LocationMapper locationMapper;
    @Autowired
    private LocationPhotoMapper locationPhotoMapper;

    @Override
    public LocationVO getWholeLocationById(Long locationId) {
        // 根据ID获取Location
        Location location = locationMapper.selectById(locationId);
        // 属性拷贝
        LocationVO locationVO = BeanUtil.copyProperties(location, LocationVO.class);
        // 根据ID获取相关地点照片信息
        List<LocationPhoto> locationPhotos = locationPhotoMapper.selectList(new QueryWrapper<LocationPhoto>()
                .eq(SchoolConstant.LOCATION_ID, locationId));
        List<String> photos = locationPhotos.stream().map(LocationPhoto::getPhoto).collect(Collectors.toList());
        // 属性封装
        locationVO.setPhotos(photos);
        return locationVO;
    }
}
