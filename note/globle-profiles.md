

# springboot 的多配置环境配置

## 一. 简述

多环境配置，这个东西无论在大厂小厂都是需要的，因为往往我们开发环境、测试环境、生产环境都不一样。数据库连接不说别的，起码 `url` 是不一样的。当然做法也有很多，可以通过 `properties` ，也可以通过 `mvn` 的配置以及 `spring` 的 `profile` 方式。

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

感觉这个 `web-starter` 就是万能的依赖，有了它什么东西都有了。

### 2. 编写不同 `profile` 的配置

共同的配置，类似于一个产量的名字啊等等均可以放在通用的配置里。

通用的配置取名是 `application.yml`

```yaml
common:
  profile: common-profile
spring:
  profiles:
    active: dev # 指定启动的环境，这里默认是开发环境
```

现在我用一个开发，一个生产环境的常亮来演示：

`application-dev.yml` 可以看到，后缀就是环境的名称：

```yaml
diff:
  profile: 123456
```

 `application-prod.yml` ：

```yaml
diff:
  profile: 789123
```

### 3. 用于读取环境常亮的控制器

简单粗暴一点，直接使用 `SPel` 去读取到不同环境的变量然后注入到控制器中返回

```java
@RestController
@RequestMapping
public class ProfileEndpoint {

    @Value("${common.profile}")
    private String common;
    @Value("${diff.profile}")
    private String diffProfile;

    @GetMapping
    public Map<String, Object> loadProfiles() {
        Map<String, Object> profileMap = new HashMap<>(2);
        profileMap.put("common", common);
        profileMap.put("diffProfile", diffProfile);
        return profileMap;
    }

}
```

### 4. 测试接口

分别以 `dev` 环境和 `prod` 环境来启动项目，分别读取不同的配置：

```
GET http://localhost:8080

HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sat, 01 Dec 2018 03:02:50 GMT

{
  "common": "common-profile",
  "diffProfile": "123456"
}

---------------------------------------------------------
GET http://localhost:8080

HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sat, 01 Dec 2018 03:03:45 GMT

{
  "common": "common-profile",
  "diffProfile": "789123"
}
```

### 5. 修改 `profile` 的方式

1. 启动时命令行传入 `--spring.profiles.active=prod` （`java -jar -Dspring.profiles.active=production demo-0.0.1-SNAPSHOT.jar`）
2. 通过修改 `application.yml` 的 `spring.profiles.active`
3. 设置系统变量 `spring.profiles.active`
4. 操作系统环境变量 `SPRING_PROFILES_ACTIVE`

以上排序优先级从高到低，也就是说，如果在命令行输入了，那么下面的设置都会被覆盖掉。我们可以利用这个特性，去做一些定制化操作，例如默认是开发环境，只有运行在服务器的时候，通过命令修改环境（一般是通过 `Jenkins` 来启动项目）

## 三. 总结

`profile` 可以让我们很快的变更环境而不需要每次部署都去关心所需要修改的配置。

















