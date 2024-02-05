package love.ytlsnb.model.school.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ula
 * @date 2024/2/5 9:26
 */
@Data
@TableName("tb_hot_search")
public class HotSearch {
    /**
     * 热点搜索主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 所属学校主键
     */
    private Long schoolId;
    /**
     * 排序字段
     */
    private Long sort;
    /**
     * 搜索记录
     */
    private String record;
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
