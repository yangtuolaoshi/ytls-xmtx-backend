package love.ytlsnb.quest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.ytlsnb.model.quest.dto.QuestQueryDTO;
import love.ytlsnb.model.quest.po.Quest;
import love.ytlsnb.model.quest.vo.QuestVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 任务持久层接口
 *
 * @author 金泓宇
 * @author 2024/2/29
 */
@Mapper
public interface QuestMapper extends BaseMapper<Quest> {
    /**
     * 分页条件查询
     * @param questQueryDTO 查询条件
     * @param page 当前页
     * @param size 每页条数
     */
    List<QuestVo> getPageByCondition(QuestQueryDTO questQueryDTO, int page, int size);

    // TODO 左右值还需要根据学校ID判断，不能把别人学校的任务也给改了
    /**
     * 添加操作更新左值
     * @param leftValue 左值，这里的左右值是
     * @param rightValue 右值
     */
    @Update("update tb_quest set left_value = (left_value + 2) where right_value > #{rightValue} and left_value > #{leftValue};")
    void addUpdateLeftValue(int leftValue, int rightValue);

    /**
     * 添加操作更新右值
     * @param rightValue 右值
     */
    @Update("update tb_quest set right_value = (right_value + 2) where right_value >= #{rightValue};")
    void addUpdateRightValue(int rightValue);

    /**
     * 删除操作更新左值
     * @param leftValue 左值
     */
    @Update("update tb_quest set left_value = (left_value - 2) where left_value > #{leftValue}")
    void deleteUpdateLeftValue(int leftValue);

    /**
     * 删除操作更新右值
     * @param rightValue 右值
     */
    @Update("update tb_quest set right_value = (right_value - 2) where right_value > #{rightValue}")
    void deleteUpdateRightValue(int rightValue);
}
