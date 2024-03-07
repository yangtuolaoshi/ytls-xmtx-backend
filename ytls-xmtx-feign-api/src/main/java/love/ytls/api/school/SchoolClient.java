package love.ytls.api.school;

import love.ytlsnb.model.school.po.Coladmin;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.school.po.Clazz;
import love.ytlsnb.model.school.po.Dept;
import love.ytlsnb.model.school.po.School;
import love.ytlsnb.model.school.po.StudentPhoto;
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

    @GetMapping("/coladmin/clazz/list/{schoolId}")
    Result<List<Clazz>> listClazzBySchoolId(@PathVariable Long schoolId);
}
