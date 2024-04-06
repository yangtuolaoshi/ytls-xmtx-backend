package love.ytlsnb.school.service;

import love.ytlsnb.model.school.po.StudentPhoto;

/**
 * @author ula
 * @date 2024/3/4 22:16
 */
public interface StudentPhotoService {
    StudentPhoto getStudentPhoto(Long studentId);
}
