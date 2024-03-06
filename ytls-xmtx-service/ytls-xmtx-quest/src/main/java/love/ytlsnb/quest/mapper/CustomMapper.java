package love.ytlsnb.quest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface CustomMapper<T> extends BaseMapper<T> {
    /**
     * 批量添加（不是for循环添加）
     * @param entityList 实体类集合
     * @return 受影响的行数
     */
    Integer insertBatchSomeColumn(List<T> entityList);
}
