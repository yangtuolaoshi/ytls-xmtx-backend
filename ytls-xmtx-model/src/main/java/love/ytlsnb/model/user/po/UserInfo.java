package love.ytlsnb.model.user.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author ula
 * @date 2024/2/4 17:44
 */
@Data
@TableName("tb_user_info")
public class UserInfo {
    /**
     * 用户信息主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 用户的真实姓名
     */
    private String name;
    /**
     * 用户性别：0 默认未设置 1 男 2 女
     */
    private Byte gender;
    /**
     * 民族
     */
    private String nationality;
    /**
     * 出生日期
     */
    private LocalDate birthDate;
    /**
     * 身份证地址
     */
    private String address;
    /**
     * 用户的身份证号
     */
    private String idNumber;
    /**
     * 用户的真实照片
     */
    private String realPhoto;
    /**
     * 用户的录取通知书照片
     */
    private String admissionLetterPhoto;
    /**
     * 用户的学校名
     */
    private String schoolName;
    /**
     * 用户的学院名
     */
    private String deptName;
    /**
     * 用户的班级名
     */
    private String clazzName;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
    /**
     * 是否删除
     */
    @TableLogic
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private Byte deleted;
}
