package cn.liweidan.web.common.dto;

/**
 * 结果响应
 *
 * @author liweidan
 * @date 2018/8/22 下午3:15
 * @email toweidan@126.com
 */
public class ResultDto<T> {

    private String code;
    private T data;
    private boolean isSuccess;
    private String errMsg;

    public ResultDto(String code, T data, boolean isSuccess, String errMsg) {
        this.code = code;
        this.data = data;
        this.isSuccess = isSuccess;
        this.errMsg = errMsg;
    }

    public String getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
