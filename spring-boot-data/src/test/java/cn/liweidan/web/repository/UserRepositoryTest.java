package cn.liweidan.web.repository;

import cn.liweidan.web.dbo.UserDo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * describe: ${describe}
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/8/29 下午8:09
 * @email toweidan@126.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private Long id;

    @Before
    public void setup() {
        UserDo userDo = new UserDo("Jane", 10);
        id = userRepository.save(userDo).getId();
    }

    @Test
    public void testFindById() {
        UserDo aUser = userRepository.findOne(id);
        assertEquals("Jane", aUser.getName());
    }

}