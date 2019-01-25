package cn.liweidan.springboot.test.endpoint;

import cn.liweidan.springboot.test.dbo.UserDo;
import cn.liweidan.springboot.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Description：用户接口层
 *
 * @author liweidan
 * @version 1.0
 * @date 2019/1/24 8:24 PM
 * @email toweidan@126.com
 */
@RestController
@RequestMapping
public class UserEndpoint {

    @Autowired
    private UserService userService;

    @PostMapping
    public void create(@RequestBody UserDo userDo) {
        userService.create(userDo);
    }

    @GetMapping("{userUID}")
    public UserDo findById(@PathVariable("userUID") String userUID) {
        return userService.findByUID(userUID);
    }

}
