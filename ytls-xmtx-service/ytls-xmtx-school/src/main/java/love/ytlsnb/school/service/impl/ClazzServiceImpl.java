package love.ytlsnb.school.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.ytlsnb.common.constants.RedisConstant;
import love.ytlsnb.common.utils.CacheClient;
import love.ytlsnb.model.school.dto.ClazzDeleteDTO;
import love.ytlsnb.model.school.dto.ClazzInsertDTO;
import love.ytlsnb.model.school.dto.ClazzQueryDTO;
import love.ytlsnb.model.school.po.Clazz;
import love.ytlsnb.model.school.po.Dept;
import love.ytlsnb.school.mapper.ClazzMapper;
import love.ytlsnb.school.mapper.DeptMapper;
import love.ytlsnb.school.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ula
 * @date 2024/2/18 9:30
 */
@Service
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private ClazzMapper clazzMapper;
    @Autowired
    private CacheClient cacheClient;

    @Override
    @Transactional
    public void addClazz(ClazzInsertDTO clazzInsertDTO) {
        // TODO clazzName唯一性校验
        // 查询相关学院信息
        Dept dept = cacheClient.query(RedisConstant.DEPT_PREFIX, clazzInsertDTO.getDeptId(), Dept.class, deptMapper::selectById);
        // 属性封装
        Clazz clazz = BeanUtil.copyProperties(clazzInsertDTO, Clazz.class);
        clazz.setDeptId(dept.getId());
        clazzMapper.insert(clazz);
    }

    @Override
    public List<Clazz> listClazzBySchoolId(Long schoolId) {
        return clazzMapper.selectList(new LambdaQueryWrapper<Clazz>()
                .eq(Clazz::getSchoolId, schoolId));
    }

    @Override
    public List<Clazz> listClazzByCondition(ClazzQueryDTO clazzQueryDTO) {
        // todo
        return clazzMapper.selectByMap(BeanUtil.beanToMap(clazzQueryDTO,false,true));
    }

    @Override
    public Boolean updateClazz(ClazzInsertDTO clazzInsertDTO) {
        Clazz clazz = BeanUtil.copyProperties(clazzInsertDTO,Clazz.class);
        return clazzMapper.insert(clazz)>0;
    }

    @Override
    public Boolean deleteClazz(ClazzDeleteDTO clazzDeleteDTO) {
        Long clazzId = clazzDeleteDTO.getId();
        clazzDeleteDTO.setDeleted((byte) 1);
        return clazzMapper.deleteById(clazzId)>0;
    }
}