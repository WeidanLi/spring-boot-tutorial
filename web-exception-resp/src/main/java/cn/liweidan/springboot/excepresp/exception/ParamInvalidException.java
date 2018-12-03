package cn.liweidan.springboot.excepresp.exception;

/**
 * Description：参数错误异常
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/20 12:26 AM
 * @email toweidan@126.com
 */
public class ParamInvalidException extends AbsException {

    public ParamInvalidException() {
        super("参数错误");
    }

}
