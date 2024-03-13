package love.ytlsnb.model.user.vo;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.ToString;
import love.ytlsnb.model.user.po.User;
import love.ytlsnb.model.user.po.UserInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author ula
 */
@Data
@ToString
public class UserVO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 主键ID
     */
    private Long userInfoId;
    /**
     * 学号
     */
    private String studentId;
    /**
     * 昵称：可重复
     */
    private String nickname;
    /**
     * 密码，采用Bcrypt加密，用户输入数据限制32位
     */
    private String password;
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
    private Long clazzId;
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

    public UserVO() {
    }

    public UserVO(User user, UserInfo userInfo) {
        BeanUtil.copyProperties(user, this);
        this.password = "********";
        BeanUtil.copyProperties(userInfo, this);
    }
}
