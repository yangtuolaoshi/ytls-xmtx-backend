package love.ytls.api.school;

import love.ytlsnb.model.common.PageResult;
import love.ytlsnb.model.reward.dto.RewardQueryDTO;
import love.ytlsnb.model.reward.vo.RewardVO;
import love.ytlsnb.model.school.dto.ClazzDeleteDTO;
import love.ytlsnb.model.school.dto.ClazzInsertDTO;
import love.ytlsnb.model.school.dto.DeptInsertDTO;
import love.ytlsnb.model.school.po.Coladmin;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.school.po.Clazz;
import love.ytlsnb.model.school.po.Dept;
import love.ytlsnb.model.school.po.School;
import love.ytlsnb.model.school.po.StudentPhoto;
import love.ytlsnb.model.school.vo.DeptVO;
import love.ytlsnb.model.school.vo.LocationVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ula
 * @date 2024/2/3 15:13
 */
@FeignClient("school-service")
public interface SchoolClient {
    @GetMapping("/coladmin/coladmin/{coladminId}")
    Result<Coladmin> getColadminById(@PathVariable Long coladminId);

    @GetMapping("/coladmin/school/list")
    Result<List<School>> list();

    @GetMapping("/coladmin/location/{locationId}")
    Result<LocationVO> getWholeLocationById(@PathVariable Long locationId);

    @GetMapping("/coladmin/studentPhoto/{userId}")
    Result<StudentPhoto> getStudentPhoto(@PathVariable Long userId);

    @GetMapping("/coladmin/school/{schoolId}")
    Result<School> getSchoolById(@PathVariable Long schoolId);

    @GetMapping("/coladmin/dept/list/{schoolId}")
    Result<List<Dept>> listDeptBySchoolId(@PathVariable Long schoolId);
    @PutMapping("/coladmin/dept/update")
    public Result<Boolean> updateDept(@RequestBody DeptInsertDTO deptInsertDTO);

    @DeleteMapping("/coladmin/dept/delete")
    public Result<Boolean>deleteDept(DeptInsertDTO deptInsertDTO);

    @RequestMapping(method = RequestMethod.GET, value = "/coladmin/dept/page")
    PageResult<List<DeptVO>> getPageByCondition(
            @SpringQueryMap DeptInsertDTO deptQueryDTO,
            @RequestParam int page,
            @RequestParam int size);

    @GetMapping("/coladmin/dept/{deptId}")
    Result<Dept> selectByDeptId(@PathVariable Long deptId);

    @GetMapping("/coladmin/clazz/list/{schoolId}")
    Result<List<Clazz>> listClazzBySchoolId(@PathVariable Long schoolId);

    @GetMapping("/coladmin/clazz/list/{schoolId}/{deptId}")
    public Result<List<Clazz>> listClazzBySchoolIdAndDeptId(@PathVariable Long schoolId, @PathVariable Long deptId);

    @PutMapping("/coladmin/clazz/update")
    Result<Boolean> updateClazz(@RequestBody ClazzInsertDTO clazzInsertDTO);
    @GetMapping("/coladmin/clazz/{clazzId}")
    Result<Clazz> getClazzById(@PathVariable Long clazzId);

    @DeleteMapping("/coladmin/clazz/delete")
    Result<Boolean>deleteClazz(ClazzDeleteDTO clazzDeleteDTO);
}
