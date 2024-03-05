package love.ytlsnb.school.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.common.constants.SchoolConstant;
import love.ytlsnb.model.school.po.StudentPhoto;
import love.ytlsnb.school.mapper.StudentPhotoMapper;
import love.ytlsnb.school.service.StudentPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ula
 * @date 2024/3/4 22:16
 */
@Slf4j
@Service
public class StudentPhotoServiceImpl extends ServiceImpl<StudentPhotoMapper, StudentPhoto> implements StudentPhotoService {
    @Autowired
    private StudentPhotoMapper studentPhotoMapper;

    @Override
    public StudentPhoto getStudentPhoto(Long userId) {
        return studentPhotoMapper.selectOne(new QueryWrapper<StudentPhoto>()
                .eq(SchoolConstant.USER_ID, userId));
    }
}
