package cn.liweidan.web.endpoint;

import cn.liweidan.web.dbo.UserDo;
import cn.liweidan.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Description：用户资源控制器
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/8/30 上午12:38
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

    @GetMapping("{id}")
    public UserDo findById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

}
