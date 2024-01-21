package love.ytlsnb.model.common;

import lombok.Data;

/**
 * 表现层结果封装
 * @param <T>
 */
@Data
public class Result<T> {
    /**
     * 状态码
     */
    private Integer code;

    /**
     * 数据
     */
    private T data;

    /**
     * 消息
     */
    private String msg;

    public Result() {
        this.code = 200;
    }

    public Result(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public Result<T> ok(T data) {
        return new Result<>(200, data, "success...");
    }

    public Result<T> fail(Integer code, String msg) {
        return new Result<>(code, null, msg);
    }
}
