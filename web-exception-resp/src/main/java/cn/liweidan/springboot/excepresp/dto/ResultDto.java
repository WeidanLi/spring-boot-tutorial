package cn.liweidan.springboot.excepresp.dto;

/**
 * Description：结果返回类
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/20 12:19 AM
 * @email toweidan@126.com
 */
public class ResultDto<T> {

    private boolean success;
    private T data;
    private String message;

    public ResultDto(T data) {
        this.success = true;
        this.data = data;
    }

    public ResultDto(String message) {
        this.success = false;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

}
