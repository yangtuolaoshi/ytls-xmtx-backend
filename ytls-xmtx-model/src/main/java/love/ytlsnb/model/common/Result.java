package love.ytlsnb.model.common;

import lombok.Data;

/**
 * 表现层结果封装
 * @param <T>
 *
 * @author 金泓宇
 * @date 2024/01/21
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
        this.msg = "success";
    }

    public Result(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static<T> Result<T> ok() {
        return new Result<>(200, null, "success");
    }
    public static<T> Result<T> ok(T data) {
        return new Result<>(200, data, "success");
    }

    public static<T> Result<T> fail(Integer code, String msg) {
        return new Result<>(code, null, msg);
    }
}
