package love.ytlsnb.common.constants;

/**
 * @author ula
 * @date 2024/2/7 16:45
 */
public class QuestConstant {
    /**
     * 任务状态字段常量
     */
    public static final String STATUS = "status";
    /**
     * 任务禁用常量
     */
    public static final Byte DISABLED = 0;
    /**
     * 任务启用常量
     */
    public static final Byte ENABLED = 1;
    /**
     * 任务所属学校ID字段常量
     */
    public static final String SCHOOL_ID = "school_id";
    /**
     * 任务父结点ID字段常量
     */
    public static final String PARENT_ID = "parent_id";
    /**
     * 每个学校都有且仅有一个根任务，该字段为根任务的父结点值，用于校验传入的新增任务对象是否为根节点
     */
    public static final Long NULL_PARENT_ID = 0L;
    /**
     * 任务左值字段常量
     */
    public static final String LEFT_VALUE = "left_value";
    /**
     * 新增根节点的左值
     */
    public static final Long ROOT_QUEST_LEFT = 1L;
    /**
     * 任务右值值字段常量
     */
    public static final String RIGHT_VALUE = "right_value";
    /**
     * 新增根节点的右值
     */
    public static final Long ROOT_QUEST_RIGHT = 2L;
}
