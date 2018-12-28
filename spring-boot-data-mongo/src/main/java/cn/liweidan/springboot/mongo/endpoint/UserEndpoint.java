package cn.liweidan.springboot.mongo.endpoint;

import cn.liweidan.springboot.mongo.dbo.UserDo;
import cn.liweidan.springboot.mongo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Description：用户接口
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/12/28 9:55 AM
 * @email toweidan@126.com
 */
@RestController
@RequestMapping("user")
public class UserEndpoint {

    private UserRepository userRepository;

    @Autowired
    public UserEndpoint(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<UserDo> findAll() {
        return userRepository.findAll();
    }

    @PostMapping
    public void addUser(@RequestBody UserDo userDo) {
        userDo.setUuid(UUID.randomUUID().toString());
        userRepository.save(userDo);
    }

    @GetMapping("{uid}")
    public UserDo findByUID(@PathVariable("uid") String uid) {
        return userRepository.findById(uid).get();
    }

}
