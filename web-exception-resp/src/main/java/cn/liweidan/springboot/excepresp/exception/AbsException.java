package cn.liweidan.springboot.excepresp.exception;

/**
 * Description：项目异常基类
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/20 12:23 AM
 * @email toweidan@126.com
 */
public class AbsException extends RuntimeException {

    public AbsException() {
    }

    public AbsException(String message) {
        super(message);
    }

}
