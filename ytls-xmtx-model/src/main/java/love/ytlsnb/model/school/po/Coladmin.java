package love.ytlsnb.model.school.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ula
 * @date 2024/2/6 16:22
 */
@Data
@TableName("tb_coladmin")
public class Coladmin {
    /**
     * 管理员账号主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 账号权限
     */
    private Byte privilege;
    /**
     * 账号状态
     */
    private Byte status;
    /**
     * 学院所属学校主键
     */
    private Long schoolId;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
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
