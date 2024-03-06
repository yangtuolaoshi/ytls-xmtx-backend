package love.ytlsnb.model.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 表现层分页结果封装
 * @param <T>
 *
 * @author 金泓宇
 * @date 2024/01/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageResult<T> extends Result<T> {
    /**
     * 当前页
     */
    private Integer page;

    /**
     * 每页条数
     */
    private Integer size;

    /**
     * 总数
     */
    private Long total;

    public PageResult() {
    }

    public PageResult(Integer page, Integer size, Long total) {
        super();
        this.page = page;
        this.size = size;
        this.total = total;
    }
}
