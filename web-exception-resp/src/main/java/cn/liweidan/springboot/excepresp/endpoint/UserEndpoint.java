package cn.liweidan.springboot.excepresp.endpoint;

import cn.liweidan.springboot.excepresp.dto.ResultDto;
import cn.liweidan.springboot.excepresp.dto.UserDto;
import cn.liweidan.springboot.excepresp.exception.ElementNotFoundException;
import cn.liweidan.springboot.excepresp.exception.ParamInvalidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description：用户资源控制器
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/20 12:26 AM
 * @email toweidan@126.com
 */
@RestController
@RequestMapping("user")
public class UserEndpoint {


    @GetMapping("{id}")
    public ResultDto<UserDto> idOf(@PathVariable("id") Long id) {
        if (id > 100) {
            throw new ParamInvalidException();
        }
        if (id % 2 == 0) {
            throw new ElementNotFoundException("用户");
        }
        UserDto userDto = new UserDto(id, "狗蛋", 18);
        return new ResultDto<>(userDto);
    }

}
