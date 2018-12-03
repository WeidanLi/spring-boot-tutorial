package cn.liweidan.springboot.excepresp.exception;

/**
 * Description：元素不存在异常
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/20 12:24 AM
 * @email toweidan@126.com
 */
public class ElementNotFoundException extends AbsException {

    public ElementNotFoundException(String elementName) {
        super(elementName + "不存在");
    }

}
