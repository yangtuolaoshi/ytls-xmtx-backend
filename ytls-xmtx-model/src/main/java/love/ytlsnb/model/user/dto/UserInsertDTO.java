package love.ytlsnb.model.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

/**
 * @author ula
 * @date 2024/2/16 14:41
 */
@Data
@ToString
public class UserInsertDTO {
    /**
     * 学生学号(必填)
     */
    private String studentId;
    /**
     * 学生真实姓名
     */
    private String name;
    /**
     * 学生性别
     */
    private Byte gender;
    /**
     * 学生民族
     */
    private String nationality;
    /**
     * 学生出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate birthDate;
    /**
     * 学生身份证上面的地址
     */
    private String address;
    /**
     * 学生身份证号
     */
    private String idNumber;
    /**
     * 学生真实照片
     */
    private String realPhoto;
    /**
     * 学生录取通知书
     */
    private String admissionLetterPhoto;
    /**
     * 学生学校ID
     */
    private Long schoolId;
    /**
     * 学生学校名
     */
    private String schoolName;
    /**
     * 学生学院ID
     */
    private Long deptId;
    /**
     * 学生学院名
     */
    private String deptName;
    /**
     * 学生班级ID
     */
    private Long clazzId;
    /**
     * 学生班级名
     */
    private String clazzName;
}
