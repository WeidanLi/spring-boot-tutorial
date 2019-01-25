package cn.liweidan.springboot.test.service;

import cn.liweidan.springboot.test.BaseTest;
import cn.liweidan.springboot.test.dbo.UserDo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import static org.junit.Assert.*;

/**
 * describe: 用户业务层的测试
 *
 * @author liweidan
 * @version 1.0
 * @date 2019/1/25 7:44 PM
 * @email toweidan@126.com
 */
public class UserServiceTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Test
    public void create() {
        // 测试传递空数据的时候是否正常抛出异常，首先传递想要出现的异常
        thrown.expect(IllegalArgumentException.class);
        // 调用实例
        userService.create(new UserDo());
        // 如果没有抛出异常，代码走到这里，将会直接抛出错误（因为不应该会走到这里来的）
        fail();

        // 可以逐个缺少参数进行测试但好像没这个必要，毕竟业务开发时间不允许那么长时间花在编写测试上面
        // 不过话说回来，这里缺失的在后面如果对参数验证进行重构的话，绝对需要补上来的

        // 编写一个正确的参数传递进去，然后需要回过头来查询插入的数据是否存在
        UserDo userDo = new UserDo();
        userDo.setUuid("1");
        userDo.setName("Adan");
        userService.create(userDo);
        // 查询是否存在，当实际项目中这里是使用仓库进行存储的时候，理应应该使用仓库进行查询
        UserDo dbUserDo = userService.findByUID("1");
        assertNotNull("用户信息不应该为null", dbUserDo);
        assertTrue("用户uuid不应为空", !StringUtils.isEmpty(dbUserDo.getUuid()));
        assertTrue("用户name不应为空", !StringUtils.isEmpty(dbUserDo.getName()));
    }

    @Test
    public void findByUID() {
        // 这里的测试步骤基本和上面差不多，不过反过来的，先使用findByUID查询，期望为找不到，
        thrown.expect(IllegalArgumentException.class);
        userService.findByUID("999");
        fail();
        // 再插入再查询，期望不为空
        UserDo userDo = new UserDo();
        userDo.setUuid("1");
        userDo.setName("Adan");
        userService.create(userDo);
        UserDo dbUserDo = userService.findByUID("1");
        assertNotNull("用户信息不应该为null", dbUserDo);
        assertTrue("用户uuid不应为空", !StringUtils.isEmpty(dbUserDo.getUuid()));
        assertTrue("用户name不应为空", !StringUtils.isEmpty(dbUserDo.getName()));
    }

}