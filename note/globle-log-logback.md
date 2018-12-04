# springboot 日志使用（`LogBack`）

## 一. 简述

在 `web` 开发中，大大小小的 `bug` 是难以避免的。这时候，记录起来的日志就可以用来解决问题或者回滚。当然日志也可以用来查询用户操作的东西，以便甩锅（刚刚说完我就接到了一个产品被认为设置成 330 元的日志查询任务，他应该是 30 元以内的）。

项目演示：`globle-log-logback`

## 二. 开发

### 1. `mvn` 依赖

```xml
<dependencies>

    <!-- 引入 web-starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

</dependencies>
```

依然是引入这个万能的 `starter` 包即可（😂）

### 2. 一个 `user` 实体类

为了掩饰这里就简单的使用 `user` 这个业务来做吧。也不设置 `service` 和 `repo` 层了。

```java
public class UserDo {

    private Long id;
    private String name;

    public UserDo(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserDo() {
    }
    
    // 省略 getter & setter
}
```

### 3. 一个 `user` 控制器

一个控制器，用于查询所有用户以及新增用户，将会在这里记录查询和新增的参数。

```java
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
        userDos.add(new UserDo((long) userDos.size(), name));
    }

}
```

在这里我建议使用 `slf4j` 去获取日志的实例，当然也不是建议啦，已经变成一种习惯了。

#### 4. 请求访问

```
2018-12-04 08:58:28.775  INFO 1068 --- [nio-8080-exec-1] c.l.s.logback.endpoint.UserEndpoint      : 查询了用户列表
2018-12-04 08:58:30.823  INFO 1068 --- [nio-8080-exec-2] c.l.s.logback.endpoint.UserEndpoint      : 新增一个用户，用户名称是XiaoMing
```

请求相对应的接口上面的日志将会被打印出来。（但还只是打印到控制台罢了）

## 三. 日志配置

日志配置的名称约定为 `logback-spring.xml` 项目启动的时候 `spring` 将会去读取这个日志配置文件。

### 1. 指定日志存储位置

像上面的范例，只是简单的在控制台记录日志，是不可行的，因为控制台的东西到了一定的大小会不断膨胀，也不利于日志的整理。所以我们一般使用每天生成一个文件的形式来记录日志。

示例中有完整的 `xml` 配置文件

```xml
<!-- 文件保存日志的相关配置，同步 -->
<appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
    <!-- 保存日志文件的路径 -->
    <file>${log.path}/info.log</file>
    <!-- 日志格式 -->
    <encoder>
        <pattern>%d{yyyy-MM-dd HH:mm:ss} %-4relative [%thread] %-5level %logger{35} - %msg %n</pattern>
    </encoder>
    <!-- 过滤其它级别输出 -->
    <!-- 循环政策：基于时间创建日志文件 -->
    <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
        <!-- 日志文件名格式 -->
        <fileNamePattern>${log.path}/info-%d{yyyy-MM-dd}.log</fileNamePattern>
    </rollingPolicy>
</appender>
```

### 2. 日志等级指定

日志等级可以在 `application.yml` 指定：

```yaml
logging:
  level:
    cn.liweidan.springboot.logback.endpoint.UserEndpoint: debug
```

比如我指定了用户控制器是 debug 等级的，然后在创建用户的时候加一个 debug 记录。那么请求用户创建就会记录下来：

```
2018-12-04 09:35:11.027  INFO 1573 --- [nio-8080-exec-1] cn.liweidan.springboot.logback.endpoint.UserEndpoint : 新增一个用户，用户名称是XiaoMing
2018-12-04 09:35:11.027 DEBUG 1573 --- [nio-8080-exec-1] cn.liweidan.springboot.logback.endpoint.UserEndpoint : 用户请求了用户创建接口
```

### 3. 日志等级分组记录

使用分组来简化 `log` 日志等级的配置

```yaml
logging: 
  group: 
    user: cn.liweidan.springboot.logback.endpoint.UserEndpoint, ...其他controller
  level:
    user: debug
```

```
2018-12-04 09:51:39.772  INFO 1709 --- [nio-8080-exec-1] cn.liweidan.springboot.logback.endpoint.UserEndpoint : 新增一个用户，用户名称是XiaoMing
2018-12-04 09:51:39.772 DEBUG 1709 --- [nio-8080-exec-1] cn.liweidan.springboot.logback.endpoint.UserEndpoint : 用户请求了用户创建接口
```

## 四. 总结

日志的配置可以这样分类：
1. 输出样式定义的放在 `xml` 中
2. 输出等级的定义放在 `application.yml` 中

