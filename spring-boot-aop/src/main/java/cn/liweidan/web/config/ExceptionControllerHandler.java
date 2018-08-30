package cn.liweidan.web.config;

import cn.liweidan.web.common.dto.ResultDto;
import cn.liweidan.web.common.ex.ItemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 异常处理控制器
 *
 * @author liweidan
 * @date 2018/8/22 下午3:14
 * @email toweidan@126.com
 */
@ControllerAdvice
@RestController
public class ExceptionControllerHandler {

    /**
     * 处理业务出现的异常
     * @param e
     * @return
     */
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResultDto<Void> itemNotFoundException(ItemNotFoundException e) {
        return new ResultDto<>("404", null, false, e.getMessage());
    }

    /**
     * 处理业务出现的异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultDto<Void> serverException(Exception e) {
        return new ResultDto<>("500", null, false, e.getMessage());
    }

}
