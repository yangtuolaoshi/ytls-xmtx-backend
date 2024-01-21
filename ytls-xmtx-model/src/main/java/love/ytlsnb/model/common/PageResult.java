package love.ytlsnb.model.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 表现层分页结果封装
 * @param <T>
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
    private Integer total;

    public PageResult(Integer page, Integer size, Integer total) {
        this.page = page;
        this.size = size;
        this.total = total;
    }
}
