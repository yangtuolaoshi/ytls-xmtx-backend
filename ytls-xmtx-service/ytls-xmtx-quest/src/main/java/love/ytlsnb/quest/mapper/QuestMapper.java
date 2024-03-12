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
    void addUpdateLeftValue(int leftValue, int rightValue, Long schoolId);

    /**
     * 添加操作更新右值
     * @param rightValue 右值
     */
    void addUpdateRightValue(int rightValue, Long schoolId);

    /**
     * 删除操作更新左值
     * @param leftValue 左值
     */
    void deleteUpdateLeftValue(int leftValue, Long schoolId);

    /**
     * 删除操作更新右值
     * @param rightValue 右值
     */
    void deleteUpdateRightValue(int rightValue, Long schoolId);
}
