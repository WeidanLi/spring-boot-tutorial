package cn.liweidan.springboot.logback.endpoint;

import cn.liweidan.springboot.logback.dbo.UserDo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：用户控制器
 *
 * @author liweidan
 * @version 1.0
 * @date 2018-12-03 17:40
 * @email toweidan@126.com
 */
@RequestMapping("user")
@RestController
public class UserEndpoint {

    List<UserDo> userDos = new ArrayList<>();

    // 使用 slf4j 去获取日志操作实例
    private static Logger logger = LoggerFactory.getLogger(UserEndpoint.class);

    public UserEndpoint() {
        userDos.add(new UserDo((long) userDos.size(), "Weidan"));
    }

    @GetMapping
    public List<UserDo> selectAll() {
        logger.info("查询了用户列表");
        return userDos;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(String name) {
        // info 记录操作
        logger.info("新增一个用户，用户名称是{}", name);
        logger.debug("用户请求了用户创建接口");
        userDos.add(new UserDo((long) userDos.size(), name));
    }

}
