package love.ytlsnb.school.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.ytlsnb.common.constants.RedisConstant;
import love.ytlsnb.common.constants.ResultCodes;
import love.ytlsnb.common.constants.SchoolConstant;
import love.ytlsnb.common.exception.BusinessException;
import love.ytlsnb.common.utils.ColadminHolder;
import love.ytlsnb.common.utils.CacheClient;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.reward.po.Reward;
import love.ytlsnb.model.reward.vo.RewardVO;
import love.ytlsnb.model.school.dto.DeptInsertDTO;
import love.ytlsnb.model.school.po.Coladmin;
import love.ytlsnb.model.school.po.Dept;
import love.ytlsnb.model.school.po.School;
import love.ytlsnb.model.school.vo.DeptVO;
import love.ytlsnb.school.mapper.DeptMapper;
import love.ytlsnb.school.mapper.SchoolMapper;
import love.ytlsnb.school.service.DeptService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static love.ytlsnb.common.constants.SchoolConstant.IS_DELETED;

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

    /**
     * 添加学院
     * @param deptInsertDTO
     */
    @Override
    @Transactional
    public void addDept(DeptInsertDTO deptInsertDTO) {
        // deptName唯一性校验
        String deptName = deptInsertDTO.getDeptName();
        if(deptName != null) {
            Dept selectOne = deptMapper.selectOne(new LambdaQueryWrapper<Dept>()
                    .eq(Dept::getDeptName,deptName));
            if(selectOne != null){
                throw new BusinessException(ResultCodes.FORBIDDEN,"已经存在该名称的学院！");
            }
        }
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

    /**
     * 查询学院
     * @param schoolId
     * @return
     */
    @Override
    public List<Dept> listDeptBySchoolId(Long schoolId) {
        return deptMapper.selectList(new LambdaQueryWrapper<Dept>()
                .eq(Dept::getSchoolId, schoolId));
    }

    /**
     * 修改学院
     * @param deptInsertDTO
     * @return
     */
    @Override
    public Boolean update(DeptInsertDTO deptInsertDTO) {
        Dept dept = new Dept();
        Coladmin coladmin = ColadminHolder.getColadmin();

        BeanUtil.copyProperties(deptInsertDTO,dept);
        dept.setUpdateTime(LocalDateTime.now());
        dept.setSchoolId(coladmin.getSchoolId());
        return deptMapper.updateById(dept)>0;
    }

    @Override
    public Boolean deleteDept(DeptInsertDTO deptInsertDTO) {
        Dept dept = new Dept();
        BeanUtil.copyProperties(deptInsertDTO,dept);
        Coladmin coladmin = ColadminHolder.getColadmin();
        dept.setSchoolId(coladmin.getSchoolId());
        dept.setDeleted(IS_DELETED);
        return deptMapper.deleteById(dept)>0;
    }

    /**
     * 分页查询
     * @param deptInsertDTO
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResult<List<DeptVO>> getPageByCondition(DeptInsertDTO deptInsertDTO, int page, int size) {
        LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
        String deptName = deptInsertDTO.getDeptName();
        if (deptName != null) {
            queryWrapper.likeRight(Dept::getDeptName, deptName);// 百分号只在右侧，防止索引失效
        }


        Coladmin coladmin = ColadminHolder.getColadmin();
        Long schoolId = coladmin.getSchoolId();
        if (schoolId != null){
            queryWrapper.eq(Dept::getSchoolId,schoolId);
        }

        List<DeptVO> deptVOS = new ArrayList();
        Long total = deptMapper.selectCount(queryWrapper);
        List<Dept> depts = deptMapper.selectList(queryWrapper);

        BeanUtils.copyProperties(depts,deptVOS);

        // 分页查询
        if (deptName != null) {
            deptInsertDTO.setDeptName(deptName + "%");
        }

        PageResult<List<DeptVO>> pageResult = new PageResult<>(page, deptVOS.size(), total);
        pageResult.setData(deptVOS);
        return pageResult;

    }


}
