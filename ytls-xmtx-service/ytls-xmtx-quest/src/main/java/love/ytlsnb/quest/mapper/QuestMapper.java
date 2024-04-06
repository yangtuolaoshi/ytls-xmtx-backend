package love.ytlsnb.quest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import love.ytlsnb.model.quest.dto.QuestQueryDTO;
import love.ytlsnb.model.quest.po.Quest;
import love.ytlsnb.model.quest.vo.QuestVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
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

    /**
     * 找到最大的树ID
     * @param schoolId 学校ID
     * @return 最大的树ID
     */
    @Select("select max(tree_id) from tb_quest where school_id = #{schoolId}")
    Integer getMaxTreeId(long schoolId);

    /**
     * 添加操作更新左值
     * @param leftValue 左值，这里的左右值是
     * @param rightValue 右值
     * @param schoolId 学校ID
     * @param treeId 树ID
     */
    void addUpdateLeftValue(int leftValue, int rightValue, long schoolId, int treeId);

    /**
     * 添加操作更新右值
     * @param rightValue 右值
     * @param schoolId 学校ID
     * @param treeId 树ID
     */
    void addUpdateRightValue(int rightValue, Long schoolId, int treeId);

    /**
     * 删除操作更新左值
     * @param leftValue 左值
     * @param schoolId 学校ID
     * @param treeId 树ID
     */
    void deleteUpdateLeftValue(int leftValue, Long schoolId, int treeId);

    /**
     * 删除操作更新右值
     * @param rightValue 右值
     * @param schoolId 学校ID
     * @param treeId 树ID
     */
    void deleteUpdateRightValue(int rightValue, Long schoolId, int treeId);
}
