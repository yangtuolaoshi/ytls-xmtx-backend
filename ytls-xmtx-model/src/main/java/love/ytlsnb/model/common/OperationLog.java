package love.ytlsnb.model.common;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ula
 * @date 2024/3/14 21:19
 */
@Data
public class OperationLog {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 操作者的ID
     */
    private Long operId;
    /**
     * 操作者的用户名
     */
    private String username;
    /**
     * 操作时间
     */
    private LocalDateTime operTime;
    /**
     * 操作的URL
     */
    private String operUrl;
    /**
     * 操作的类
     */
    private String operClazz;
    /**
     * 操作的方法
     */
    private String operMethod;
    /**
     * 操作的类型
     */
    private String operType;
    /**
     * 操作的状态:0 失败 1 成功
     */
    private Byte operStatus;
    /**
     * 操作成功返回的信息对象
     */
    private String operDetail;
    /**
     * 操作失败的异常信息
     */
    private String errorMsg;

}
