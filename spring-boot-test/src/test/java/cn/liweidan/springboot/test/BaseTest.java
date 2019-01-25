package cn.liweidan.springboot.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Description：测试基类
 * 测试基类在类级别上主要定义了测试基类的安装（一般就是 SpringBootTest 的一些注解）
 * 安装数据
 * 清理数据等等
 *
 * @author liweidan
 * @version 1.0
 * @date 2019/1/25 7:34 PM
 * @email toweidan@126.com
 */
@SpringBootTest(classes = TestApplication.class) // 定义这个类是一个 SpringBoot 项目的测试，指定启动器（当然只有一个的话不指定也是可以的）
@RunWith(SpringRunner.class) // 使用 SpringRunner 进行运行，常规写法，照抄就可以了
// @Transactional // 如果存在数据库，可以使用该注解定义测试过后是否回滚数据，这么写是会自动回滚的，如果不想要回滚，可以省略该注解
public abstract class BaseTest {

    /**
     * 测试是否按需抛出异常
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * 测试mvn层接口套件
     */
    @Autowired
    private WebApplicationContext context;
    protected MockMvc mvc;

    @Before
    public void setUp() {
        // 在测试用来开始的时候装配 MockMvc 套件
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

}
