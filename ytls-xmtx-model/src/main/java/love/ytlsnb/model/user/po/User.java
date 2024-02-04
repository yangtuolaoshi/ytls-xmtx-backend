package love.ytlsnb.model.user.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import love.ytlsnb.model.user.dto.UserRegisterDTO;

import java.time.LocalDateTime;

/**
 * 用户类
 *
 * @author 金泓宇
 * @date 2024/1/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_user")
public class User {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 学号
     */
    private String studentId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 密码，采用Bcrypt加密，用户输入数据限制32位
     */
    private String password;
    /**
     * 性别：0未设置 1男 2女
     */
    private Byte gender;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 手机号：最大长度11位
     */
    private String phone;
    /**
     * 个性签名：最大长度256位
     */
    private String sign;
    /**
     * 积分
     */
    private Long point;
    /**
     * 是否认证
     */
    @TableField("is_identified")
    private Byte identified;
    /**
     * 用户学校ID
     */
    private Long schoolId;
    /**
     * 用户部门ID
     */
    private Long deptId;
    /**
     * 用户班级ID
     */
    private Long classId;
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

    public User(UserRegisterDTO userRegisterDTO) {
        this.studentId = userRegisterDTO.getStudentId();
        this.nickname = userRegisterDTO.getNickname();
        this.password = userRegisterDTO.getPassword();
        this.phone = userRegisterDTO.getPhone();
    }
}
