package cn.liweidan.springboot.mybatis.endpoint;

import cn.liweidan.springboot.mybatis.dbo.UserDo;
import cn.liweidan.springboot.mybatis.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Description：用户控制器
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/12/27 5:59 PM
 * @email toweidan@126.com
 */
@RestController
@RequestMapping("user")
public class UserEndpoint {

    private UserMapper userMapper;

    @Autowired
    public UserEndpoint(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping
    public PageInfo<UserDo> selectAll() {
        PageHelper.startPage(1, 1);
        return new PageInfo<>(userMapper.selectAllUser());
    }

    @PostMapping
    public void addUser(@RequestBody UserDo userDo) {
        userDo.setUuid(UUID.randomUUID().toString());
        userMapper.addUser(userDo);
    }

    @GetMapping("{uid}")
    public UserDo selectByUId(@PathVariable("uid") String uid) {
        return userMapper.selectByPrimaryKey(uid);
    }

}
