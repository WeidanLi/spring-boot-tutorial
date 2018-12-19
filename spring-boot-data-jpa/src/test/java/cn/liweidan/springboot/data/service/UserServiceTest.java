package cn.liweidan.springboot.data.service;

import cn.liweidan.springboot.data.dbo.UserDo;
import cn.liweidan.springboot.data.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * describe: ${describe}
 *
 * @author liweidan
 * @version 1.0
 * @date 2018-12-19 09:50
 * @email toweidan@126.com
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional // 可以在测试完成的时候回滚数据
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void add() {
        UserDo userDo = new UserDo();
        userDo.setName("狗蛋");
        userService.add(userDo);

        long count = userRepository.findAll().stream()
                .filter(userDo1 -> userDo1.getName().equals(userDo.getName()))
                .count();
        assertTrue("should have a user who name is " + userDo.getName(), count > 0);
    }

    @Test
    public void update() {
        UserDo userDo = new UserDo();
        userDo.setName("狗剩");
        userService.add(userDo);

        Optional<UserDo> optionalUserDo = userRepository.findAll()
                .stream().findAny();
        optionalUserDo.ifPresent(userDo1 -> {
            UserDo update = new UserDo();
            update.setId(userDo1.getId());
            update.setName("狗仔");
            userService.update(update);
        });

        Optional<UserDo> aDo = userRepository.findAll()
                .stream().filter(query -> query.getName().equals("狗仔")).findAny();
        assertTrue("should have a user who name is 狗仔", aDo.isPresent());
    }

    @Test
    public void delete() {
        UserDo userDo = new UserDo();
        userDo.setName("狗剩");
        userService.add(userDo);

        Optional<UserDo> optionalUserDo = userRepository.findAll()
                .stream().findAny();
        Long id = optionalUserDo.get().getId();
        userService.delete(id);
        long count = userRepository.findAll()
                .stream().filter(userDo1 -> userDo1.getId().equals(id))
                .count();
        assertTrue("should not have a user who id is " + id, count < 1);

    }

    @Test
    public void getById() {
        UserDo userDo = new UserDo();
        userDo.setName("狗剩");
        userService.add(userDo);

        Optional<UserDo> optionalUserDo = userRepository.findAll()
                .stream().findAny();
        UserDo byId = userService.getById(optionalUserDo.get().getId());
        assertNotNull("result should not be null", byId);
    }
}