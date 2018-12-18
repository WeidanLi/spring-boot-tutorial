package cn.liweidan.springboot.validate.endpoint;

import cn.liweidan.springboot.validate.dto.Add;
import cn.liweidan.springboot.validate.dto.Update;
import cn.liweidan.springboot.validate.dto.UserDto;
import cn.liweidan.springboot.validate.validator.Chs;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@Validated // 类上添加该注解，能够验证直接使用参数形式的借口
public class UserEndpoint {

    /**
     * 如果需要验证Dto类，只需要加上@Validated即可，如需分组，传入参数
     * @param userDto
     */
    @PostMapping
    public void create(@Validated({Add.class}) @RequestBody UserDto userDto) {
        System.out.println("-----> add: " + userDto);
    }

    @GetMapping
    public UserDto queryByName(@Length(min = 1, max = 8) @RequestParam(value = "name") String name) {
        System.out.println(name);
        UserDto userDto = new UserDto();
        userDto.setId(100);
        userDto.setUserName("狗蛋");
        userDto.setAge(19);
        return userDto;
    }

    @PutMapping
    public void update(@Validated({Update.class}) @RequestBody UserDto userDto) {
        System.out.println("-----> update: " + userDto);
    }

    @PostMapping("testChsValidator")
    public void testChsValidator(@Chs @RequestParam String chsName) {

    }

}
