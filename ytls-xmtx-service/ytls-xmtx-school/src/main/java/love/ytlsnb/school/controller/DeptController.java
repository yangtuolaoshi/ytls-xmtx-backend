package love.ytlsnb.school.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.reward.dto.RewardQueryDTO;
import love.ytlsnb.model.reward.vo.RewardVO;
import love.ytlsnb.model.school.dto.DeptInsertDTO;
import love.ytlsnb.model.school.po.Dept;
import love.ytlsnb.model.school.vo.DeptVO;
import love.ytlsnb.school.service.DeptService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ula
 * @date 2024/2/20 8:32
 */
@Slf4j
@RestController
@RequestMapping("/dept")
public class DeptController {
    @Autowired
    private DeptService deptService;

    @PostMapping("/dept")
    public Result addDept(@RequestBody DeptInsertDTO deptInsertDTO) {
        log.info("新增学院:{}", deptInsertDTO);
        deptService.addDept(deptInsertDTO);
        return Result.ok();
    }

    @GetMapping("/all")
    public Result<List<Dept>> listDeptBySchoolId() {
        log.info("根据学校Id获取所有的学院数据");
        List<Dept> deptList = deptService.listDeptBySchoolId();
        return Result.ok(deptList);
    }

    @GetMapping("/{deptId}")
    public Result<Dept> selectByDeptId(@PathVariable Long deptId){
        Dept dept = deptService.selectByDeptId(deptId);
        return Result.ok(dept);
    }

    @PutMapping("/update")
    public Result<Boolean> updateDept(@RequestBody DeptInsertDTO deptInsertDTO){

        return Result.ok(deptService.update(deptInsertDTO));
    }

    @DeleteMapping("/delete")
    public Result<Boolean>deleteDept(DeptInsertDTO deptInsertDTO){
        return Result.ok(deptService.deleteDept(deptInsertDTO));
    }

    @GetMapping("/page")
    public PageResult<List<DeptVO>> getPageByCondition(DeptInsertDTO deptQueryDTO, int page, int size){
        log.info("分页查询奖品:{}",deptQueryDTO);
        return deptService.getPageByCondition(deptQueryDTO, page, size);

    }
}
