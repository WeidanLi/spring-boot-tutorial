# 测试你的 springboot 项目

* [测试你的 springboot 项目](#测试你的-springboot-项目)
  * [一. 简述](#一-简述)
  * [二. 开发](#二-开发)
    * [1. 引入测试库](#1-引入测试库)
    * [2. 准备简单的业务环境](#2-准备简单的业务环境)
    * [3. 开始测试](#3-开始测试)
      * [1）编写测试环境的配置文件](#1）编写测试环境的配置文件)
      * [2）编写测试基类](#2）编写测试基类)
      * [3）测试业务层](#3）测试业务层)
      * [4）测试接口层](#4）测试接口层)
  * [三. 总结](#三-总结)

## 一. 简述

测试用例，在日常开发来说，可能刚开始写代码的小厂不会在乎，不过也有很多公司根本就没有测试用例，开发的时候一直写下去。写完一部分，直接启动项目，调用接口进行 `debug` 调试，然后修改报错的地方。等到了所有功能都实现了以后，就可以合并分支并且发布给测试环境去测试了。

以前刚开始我也是没有写测试用例的习惯，慢慢的接触了 `TDD` 开发模式，简直被深深吸引住了。测试驱动开发的步骤简单的说就是先写出来你期待的功能，期待的返回值，然后开始启动项目进行运行，由程序自动识别返回的结果是否是错误的。小步开发，一直到功能完成为止。

乍一听好像没什么用处，但是好处还在后面，当部门经理提出来这个功能需要加入新的需求的时候，这时候测试用例就是开发人员的利器了。在开始加入新功能之前，保证测试用例正常通过，然后开始小步加入新的需求，修改新需求结果的用例，知道满足新的需求为止。这个过程开发体验是及其舒服的，测试用例可以保证程序的正常运行，及早发现漏洞和错误。暂时把新加入需求这一项放一边，在后面看到这段代码想要重构的时候，测试用例就是心中的奠基石，修改代码的结果，然后运行测试用例，顺利通过，心情愉悦的提交代码。

ok，说了这么多测试的好处，那就要看看怎么实施了，怎么结合 `springboot` 来编写运行我们的测试用例。`web` 的测试来说，应该是要复杂一点，应为涉及到容器的启动以及正确初始化，常规的整合 `spring` 框架那么自动化注入也是测试中的一部分，所以不妨每次测试用例开始的时候，运行 `spring` 让其正确的进行运行，再测试我们的业务代码，岂不美哉。

**示例代码：spring-boot-test**

## 二. 开发

### 1. 引入测试库

```xml

<dependencies>

    <!-- springboot web starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- SpringBoot 测试 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

</dependencies>
```



### 2. 准备简单的业务环境

`UserDo` 类：

```java
public class UserDo {

    private String uuid;

    private String name;

    // 省略 getter and setter
}
```

`UserService` 类：

```java
@Service
public class UserService {

    private Map<String, UserDo> userDB = new HashMap<>();

    public void create(UserDo userDo) {
        if (StringUtils.isEmpty(userDo.getUuid())) {
            throw new IllegalArgumentException("用户uuid不能为空");
        }
        if (StringUtils.isEmpty(userDo.getName())) {
            throw new IllegalArgumentException("用户姓名不能为空");
        }
        userDB.put(userDo.getUuid(), userDo);
    }

    public UserDo findByUID(String UID) {
        UserDo userDo = userDB.get(UID);
        if (Objects.isNull(userDo)) {
            throw new IllegalArgumentException("用户不存在");
        }
        return userDo;
    }

}
```

`UserEndpoint` 接口类：

```java
@RestController
@RequestMapping
public class UserEndpoint {                                                              ··`

    @Autowired
    private UserService userService;

    @PostMapping
    public void create(@RequestBody UserDo userDo) {
        userService.create(userDo);
    }

    @GetMapping("{userUID}")
    public UserDo findById(@PathVariable("userUID") String userUID) {
        return userService.findByUID(userUID);
    }

}
```



简单的说就是实现了两个接口，一个是新增用户的接口，如果用户的两个属性有一个为空，将会抛出错误参数的属性。另外一个是查询，如果查询不到也会抛出错误参数的信息（此步日常中应该是 `NotFoundException` ）。

### 3. 开始测试

#### 1）编写测试环境的配置文件

我们知道，`mvn` 或 `gradle` 提供了测试包的 `resources` 可以隔离生产环境的配置文件，当然如果觉得生产环境的配置文件已经配置了一些测试的配置，此处可以略过，因为如果读取不到测试包下的 `resources` ，测试会自动读取生产下的 `resources` 。这里为了省时省力，也好像没什么需要特别指出的，我就不写了（主要还是因为懒...）

哎呀不行不行，还是要简单说下配置吧。像 `springboot` 测试环境下的配置，我觉得几点是我踩坑来的。比如当前项目调用了第三方项目，可以在测试配置里面写打桩的配置（比如使用了 `spring-cloud-contract` ），数据库的配置可以直接连接内存数据库，一来呢比较快，二来呢也不会存储测试过的垃圾数据（就是说会影响下一次测试的数据）。当然像连接第三方中间件的方式，目前来说，Emm，好像还没什么比较好的解决方案，就是直接连接真实存在的链接。所以我们公司在开发的时候，测试的流程中基本连接公司本地的测试服务器的中间件。

#### 2）编写测试基类

测试基类呢，主要是关于配置，关于测试前需要准备的数据，每个测试用例过后需要清理动作的抽象，由子测试类继承，拥有基类的配置以及常用的数据。

我这里就是简单的装配一些套件，当测试的一个范围里面总是需要一部分测试数据的时候，可以在这里进行装配。

```java
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
@Transactional // 测试过后是否回滚数据，这么写是会自动回滚的，如果不想要回滚，可以省略该注解
public abstract class BaseTest {

    /**
     * 测试是否按需抛出异常
     */
    @Rule
    protected ExpectedException thrown = ExpectedException.none();

    /**
     * 测试mvn层接口套件
     */
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() {
        // 在测试用来开始的时候装配 MockMvc 套件
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
    
}
```

#### 3）测试业务层

OK，那么我们现在可以开始编写我们自己代码的测试用例了。

```java
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
```

测试用例其实也不怎么难吧，其实可以说一种心理来编写是最好的，就是你别当做代码是你自己写的，你当做是别人写的，然后你现在要找茬，为难这个方法调用，写出来的测试用例就有用多了。

还有一点，有些同事喜欢直接打印出来结果，然后用肉眼去看。其实我刚开始也是，不过这种方式很痛苦，你需要在控制台一个一个去寻找，然后在脑袋里面去判断这个值是否正确。那为什么不解放你的脑袋，让程序来做呢，这就是 `Assert.assertXXX` 的用途了，使用他来判断，测试运行的时候，完全可以当成放松自己的脑袋。当所有测试用例慢慢的一个一个通过的时候，全部绿色，那就皆大欢喜了，有红色了，一个一个来修正然后重复运行。直到所有测试用例通过。说实在的，我很享受全部绿色给我的那种激励感，完全可以让我更有信心的去做另外一个任务了。

#### 4）测试接口层

老实讲，我很少测试接口层，因为公司的需求就是只要请求到达我们的服务的时候，都是需要返回 `200` 状态码的，那么这个时候，运行测试用例来测试控制器层，对于我来说只会加大我的判断力度，我需要拿到结果再去判断我们的业务代码，所以显得有点多余了。

那么如果你们公司使用的是 `RESTful` 规范的时候，那么恭喜你，这个测试你完全可以写的很舒适。我就简单演示一下接口层的测试吧。

因为只是简单的演示项目，所以测试接口显得有点吃力。比如，查找不到数据的时候应该返回 `404` 但是我这里没做接口层的监听，所以根本做不了。按照现在来看，接口层的测试一般是测试状态码是否正常返回，以及数据是否正常的返回。

```java
public class UserEndpointTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Test
    public void findById() throws Exception {
        UserDo userDo = new UserDo();
        userDo.setUuid("1");
        userDo.setName("Adan");
        userService.create(userDo);

        ObjectMapper mapper = new ObjectMapper();
        mvc.perform(MockMvcRequestBuilders.get("/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> {
                    String contentAsString = mvcResult.getResponse().getContentAsString();
                    UserDo respUserDo = mapper.readValue(contentAsString, UserDo.class);
                    assertNotNull("用户信息不能为空", respUserDo);
                })
                .andDo(MockMvcResultHandlers.print());
    }

}
```



## 三. 总结

编写项目的代码的同时，可以结合测试驱动开发编写代码，先写测试用例再写代码体，逐步让测试用例通过，这时候测试用例将变得很强大还完善，代码也不会再是孤单存在着。测试用例完善了，重构起来就显得特别方便。

在我心中完整的测试应该是项目中分层，每一层都有自己独特的测试用例，下层暴露给上层接口，这时候接口都是被测试过的，所以不害怕被调用。但是可能因为项目开发的时间并不多，所以我使用起来还是蛮吃力的。只能在业务层上做测试。不过按照日常贫血性开发，下一层也并没有东西可以测试了，项目最主要的逻辑都落在了业务层，所以好像并没有什么毛病。























