package love.ytlsnb.school.controller;

import lombok.extern.slf4j.Slf4j;
import love.ytlsnb.model.common.Result;
import love.ytlsnb.model.school.po.StudentPhoto;
import love.ytlsnb.school.service.StudentPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ula
 * @date 2024/3/5 12:38
 */
@Slf4j
@RestController
@RequestMapping("/studentPhoto")
public class StudentPhotoController {
    @Autowired
    private StudentPhotoService studentPhotoService;

    @GetMapping("/{userId}")
    public Result<StudentPhoto> getStudentPhoto(@PathVariable Long userId) {
        log.info("获取学生照片:{}", userId);
        StudentPhoto studentPhoto = studentPhotoService.getStudentPhoto(userId);
        return Result.ok(studentPhoto);
    }
}
