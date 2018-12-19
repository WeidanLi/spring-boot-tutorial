package cn.liweidan.web.endpoint;

import cn.liweidan.web.dbo.UserDo;
import cn.liweidan.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description：用户资源
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/8/29 下午7:45
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
    @ResponseStatus(HttpStatus.OK)
    public List<UserDo> all() {
        return userRepository.findAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDo idOf(@PathVariable("id") Long id) {
        return userRepository.findById(id).get();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDo save(@RequestBody UserDo userDo) {
        return userRepository.saveAndFlush(userDo);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDo update(@RequestBody UserDo userDo) {
        return userRepository.saveAndFlush(userDo);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
    }

    @GetMapping("name/{name}")
    public List<UserDo> findByName(@PathVariable("name") String name) {
        return userRepository.findByName(name);
    }

}
