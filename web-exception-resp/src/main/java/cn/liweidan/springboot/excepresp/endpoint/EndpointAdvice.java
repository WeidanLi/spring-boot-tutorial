package cn.liweidan.springboot.excepresp.endpoint;

import cn.liweidan.springboot.excepresp.dto.ResultDto;
import cn.liweidan.springboot.excepresp.exception.ElementNotFoundException;
import cn.liweidan.springboot.excepresp.exception.ParamInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description：控制器监听
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/20 12:37 AM
 * @email toweidan@126.com
 */
@ControllerAdvice // 指定这个类是一个监听类，用于监听不同异常输出结果
@RestController
public class EndpointAdvice {

    @ExceptionHandler(ElementNotFoundException.class) // 元素未找到异常
    @ResponseStatus(HttpStatus.NOT_FOUND) // 返回 404
    public ResultDto<Void> elementNotFount(ElementNotFoundException e) {
        return new ResultDto<>(e.getMessage());
    }

    @ExceptionHandler(ParamInvalidException.class) // 参数错误异常
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 返回 400
    public ResultDto<Void> paramInvalid(ParamInvalidException e) {
        return new ResultDto<>(e.getMessage());
    }

    @ExceptionHandler(Exception.class) // 未知错误
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 返回 500
    public ResultDto<Void> other(Exception e) {
        return new ResultDto<>(e.getMessage());
    }

}
