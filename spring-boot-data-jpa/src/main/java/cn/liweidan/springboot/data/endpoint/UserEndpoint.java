package cn.liweidan.springboot.data.endpoint;

import cn.liweidan.springboot.data.dbo.UserDo;
import cn.liweidan.springboot.data.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Description：用户接口层
 *
 * @author liweidan
 * @version 1.0
 * @date 2018-12-19 08:45
 * @email toweidan@126.com
 */
@RestController
@RequestMapping("user")
public class UserEndpoint {

    private UserService userService;

    @Autowired
    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody UserDo userDo) {
        userService.add(userDo);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody UserDo userDo) {
        userService.update(userDo);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long userId) {
        userService.delete(userId);
    }

    @GetMapping("{id}")
    public UserDo getById(@PathVariable("id") Long userId) {
        return userService.getById(userId);
    }

}
