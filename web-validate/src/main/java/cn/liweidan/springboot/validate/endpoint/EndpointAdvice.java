package cn.liweidan.springboot.validate.endpoint;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * Description：控制器监听
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/20 12:37 AM
 * @email toweidan@126.com
 */
@ControllerAdvice
@RestController
public class EndpointAdvice {

    @ExceptionHandler(ConstraintViolationException.class) // 参数验证异常
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 返回 400
    public Map<String, String> errorParam(ConstraintViolationException e) {
        Map<String, String> error = new HashMap<>(2);
        error.put("message", e.getMessage());
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // DTO 验证异常
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 返回 400
    public Map<String, String> errorDto(MethodArgumentNotValidException e) {
        Map<String, String> error = new HashMap<>(2);
        StringBuilder message = new StringBuilder();
        for (ObjectError allError : e.getBindingResult().getAllErrors()) {
            message.append(allError.getDefaultMessage()).append("; ");
        }
        error.put("message", message.toString());
        return error;
    }

}
