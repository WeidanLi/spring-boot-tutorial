# SpringBoot 与 jpa 的简单使用


* [SpringBoot 与 jpa 的简单使用](#springboot-与-jpa-的简单使用)
  * [一. 简述](#一-简述)
  * [二. 开发](#二-开发)
    * [1. mvn 依赖](#1-mvn-依赖)
    * [2. 开发用户接口](#2-开发用户接口)
    * [3. 用户业务层](#3-用户业务层)
    * [4. 用户DBO类](#4-用户dbo类)
    * [5. 用户仓库类](#5-用户仓库类)
    * [6. 配置文件](#6-配置文件)
    * [7.  请求接口](#7--请求接口)
    * [8. 测试业务层](#8-测试业务层)
  * [三. 总结](#三-总结)


## 一. 简述

`jpa` 全称 `javax.persistence.annotation` ，刚开始是由 `hibernate` 作者开发，后面被 `java` 公司收入到规范之中。`spring-boot-data-jpa` 就是在这套规范上面建立起来的。所以开发的时候默认的 `orm` 框架就是 `hibernate` 

在刚开始接触编程的时候，万事以快为主，但其实现在我感觉开发体验更加重要。`mybatis` 是个封装比较少的框架，速度会略胜一筹，但 `hibernate` 开发数据库的时候会显得更加面向对象。当然两者都可以在不同的业务需求中体现其重要性。

简单的开发区别就是，把 `hibernate` 框架的 `repository` 当成一个集合来使用，使用起来就更加的得心应手。`mybatis` 则更加的面向过程，自主控制 `SQL` 的运行。

## 二. 开发

我们就通过一个简单用户系统来开发

### 1. mvn 依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- 引入 jpa 注解 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- mysql依赖 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>

    <!-- 为了方便测试加入端口检测包 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <!-- SpringBoot 测试 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
	
    <!-- 为了测试的时候使用内存数据库 -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### 2. 开发用户接口

```java
@RestController
@RequestMapping("user")
public class UserEndpoint {

    private UserService userService;

    @Autowired
    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody UserDo userDo) {
        userService.add(userDo);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody UserDo userDo) {
        userService.update(userDo);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long userId) {
        userService.delete(userId);
    }

    @GetMapping("{id}")
    public UserDo getById(@PathVariable("id") Long userId) {
        return userService.getById(userId);
    }

}
```

### 3. 用户业务层

关于为什么使用业务层做示例呢，因为直接接口请求仓库没有事务边界，在使用 `update` 相关方法的时候，并不能帮我自动刷新到数据库

```java
@Service
@Transactional
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void add(UserDo userDo) {
        userRepository.save(userDo);
    }

    public void update(UserDo userDo) {
        Optional<UserDo> origin = userRepository.findById(userDo.getId());
        origin.ifPresent(originUserDo -> originUserDo.setName(userDo.getName()));
    }

    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    public UserDo getById(Long userId) {
        return userRepository.findById(userId)
                .orElse(null);
    }

}
```

### 4. 用户DBO类

```java
@Entity // 指定这是一个数据库映射类
@Table(name = "user_db") // 表的基本信息
public class UserDo {

    @Id // 规定这是一个主键
    @Column(name = "user_id") // column 的定义
    @GeneratedValue(strategy= GenerationType.IDENTITY) // 使用数据库 id 自增的功能
    private Long id;

    @Column(name = "user_name", length = 50)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserDo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
```

### 5. 用户仓库类

只需要通过继承接口的方式去获取他应有的方法

```java
@Repository
public interface UserRepository extends JpaRepository<UserDo, Long> {
}
```

`JpaRepository` 接口的方法

```java
public interface JpaRepository<T, ID> extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {
    List<T> findAll();

    List<T> findAll(Sort var1);

    List<T> findAllById(Iterable<ID> var1);

    <S extends T> List<S> saveAll(Iterable<S> var1);

    void flush();

    <S extends T> S saveAndFlush(S var1);

    void deleteInBatch(Iterable<T> var1);

    void deleteAllInBatch();

    T getOne(ID var1);

    <S extends T> List<S> findAll(Example<S> var1);

    <S extends T> List<S> findAll(Example<S> var1, Sort var2);
}
```

### 6. 配置文件

```yaml
spring:
  application:
    name: spring-boot-data-jpa
  jpa:
    hibernate:
      ddl-auto: create-drop # 定义数据库生成策略
    show-sql: true
    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect # 设置 innoDB 引擎
  datasource:
    username: root
    password: root
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/jpa_data?useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true
```

### 7.  请求接口

```
POST http://127.0.0.1:8080/user

HTTP/1.1 201 
Content-Length: 0
Date: Wed, 19 Dec 2018 02:42:16 GMT

<Response body is empty>

Response code: 201; Time: 70ms; Content length: 0 bytes
-------------------------------------------------------
GET http://127.0.0.1:8080/user/1

HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Wed, 19 Dec 2018 02:42:41 GMT

{
  "id": 1,
  "name": "狗娃"
}

Response code: 200; Time: 31ms; Content length: 20 bytes
-------------------------------------------------------
PUT http://127.0.0.1:8080/user
Content-Type: application/json

{
  "id": 1,
  "name": "狗娃"
}

HTTP/1.1 204 
Date: Wed, 19 Dec 2018 02:43:06 GMT

<Response body is empty>

Response code: 204; Time: 21ms; Content length: 0 bytes
-------------------------------------------------------
GET http://127.0.0.1:8080/user/1

HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Wed, 19 Dec 2018 02:43:24 GMT

{
  "id": 1,
  "name": "狗娃"
}

Response code: 200; Time: 39ms; Content length: 20 bytes
-------------------------------------------------------
DELETE http://127.0.0.1:8080/user/1

HTTP/1.1 204 
Date: Wed, 19 Dec 2018 02:49:49 GMT

<Response body is empty>

Response code: 204; Time: 128ms; Content length: 0 bytes
```

### 8. 测试业务层

通常来说，按照 `TDD` 的开发模式，都需要有测试用例，在日常开发中我习惯测试业务层（没那么多时间覆盖所有测试）。所以现在按照测试业务层做示例。

测试用例可以使用 `MySQL` 来做也可以用内存数据库来做，我演示是使用内存数据库来做的。

```java
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
```

`test/resources` 放置 `Spring-Boot` 的配置文件：

```yaml
spring:
  application:
    name: spring-boot-data-jpa
  jpa:
    hibernate:
      ddl-auto: create-drop # 定义数据库生成策略，启动就创建 关闭就删除
    show-sql: true
    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect # 设置 innoDB 引擎
  datasource:
    username: root
    password: root
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:scope_test_db;MODE=MYSQL;DB_CLOSE_DELAY=-1
```

OK，运行完整测试即可。

## 三. 总结

使用 `spring-boot-data-jpa` 应该尽量避免使用 `SQL` 来编写，会出现一些不可控的问题。尽量使用 `jpa` 提供的接口来做增删查改操作，后期迁移数据库的时候也不会显得无力（当然这种事情很少发生）。















