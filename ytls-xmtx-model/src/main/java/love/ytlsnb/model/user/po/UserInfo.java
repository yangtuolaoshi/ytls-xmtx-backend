package love.ytlsnb.model.user.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

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
     * 用户的身份证号
     */
    private String idCard;
    /**
     * 用户生日
     */
    private LocalDateTime birthday;
    /**
     * 用户的真实照片
     */
    private String realPhoto;
    /**
     * 用户的学校认证照片
     */
    private String authenticationPhoto;
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
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 是否删除
     */
    @TableLogic
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private Byte deleted;
}
