package cn.liweidan.springboot.redis.endpoint;

import cn.liweidan.springboot.redis.dbo.UserDo;
import cn.liweidan.springboot.redis.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Description：用户信息使用缓存注解示例控制器
 *
 * @author liweidan
 * @version 1.0
 * @date 2019/1/7 4:54 PM
 * @email toweidan@126.com
 */
@RestController
@RequestMapping("usercache")
public class UserCacheEndpoint {

    private UserService userService;

    @Autowired
    public UserCacheEndpoint(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{uid}")
    public UserDo uuidOf(@PathVariable String uid) throws IOException {
        return userService.uuidOf(uid);
    }

    @PostMapping
    public void add(@RequestBody UserDo userDo) throws JsonProcessingException {
        userService.add(userDo);
    }

    @DeleteMapping("{uid}")
    public void del(@PathVariable String uid) {
        userService.deyByUuid(uid);
    }

    @GetMapping("testUuidOfWithMutipleCache/{uid}")
    public UserDo testUuidOfWithMutipleCache(@PathVariable String uid) throws IOException {
        return userService.testUuidOfWithMutipleCache(uid);
    }

}
