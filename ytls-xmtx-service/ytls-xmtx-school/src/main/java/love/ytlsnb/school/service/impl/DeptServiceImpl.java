package love.ytlsnb.school.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.ytlsnb.common.constants.RedisConstant;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.SchoolConstant;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.utils.ColadminHolder;
import love.ytlsnb.common.utils.CacheClient;
import love.ytlsnb.model.school.dto.DeptInsertDTO;
import love.ytlsnb.model.school.po.Coladmin;
import love.ytlsnb.model.school.po.Dept;
import love.ytlsnb.model.school.po.School;
import love.ytlsnb.school.mapper.DeptMapper;
import love.ytlsnb.school.mapper.SchoolMapper;
import love.ytlsnb.school.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ula
 * @date 2024/2/18 9:28
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {
    @Autowired
    private CacheClient cacheClient;
    @Autowired
    private SchoolMapper schoolMapper;
    @Autowired
    private DeptMapper deptMapper;

    @Override
    @Transactional
    public void addDept(DeptInsertDTO deptInsertDTO) {
        // TODO deptName唯一性校验
        Coladmin coladmin = ColadminHolder.getColadmin();
        Long schoolId = coladmin.getSchoolId();
        School school = cacheClient.queryWithLogicalExpiration(RedisConstant.SCHOOL_PREFIX, schoolId, School.class, schoolMapper::selectById);
        if (school == null) {
            throw new BusinessException(ResultCodes.SERVER_ERROR, "相关学校信息不存在");
        }
        Dept dept = BeanUtil.copyProperties(deptInsertDTO, Dept.class);
        dept.setSchoolId(schoolId);
        deptMapper.insert(dept);
    }

    @Override
    public List<Dept> listDeptBySchoolId() {
        Long schoolId = ColadminHolder.getColadmin().getSchoolId();
        return deptMapper.selectList(new LambdaQueryWrapper<Dept>()
                .eq(Dept::getSchoolId, schoolId));
    }
}
