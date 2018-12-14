# spring-boot 使用 aop

* [spring-boot 使用 aop](#spring-boot-使用-aop)
  * [一. 简述](#一-简述)
  * [二. 开发](#二-开发)
    * [1. 引入 aop 依赖](#1-引入-aop-依赖)
    * [2. 使用注解切入](#2-使用注解切入)
    * [3. 开发一个用户控制器以及用户传输类](#3-开发一个用户控制器以及用户传输类)
    * [4. 开发一个用户业务层](#4-开发一个用户业务层)
    * [5. 请求访问](#5-请求访问)

## 一. 简述

`AOP` 是一种热门的编程模式，用途十分广泛，可以作为日志的记录、事件订阅。

主要的方式是通过 `Aspect` 框架，将与项目无太大关联的但是每个方法又都需要做的事情给抽取到一个类里面。然后使用 `IOC` 容器调用方法之前之后或者抛异常以后，跳入指定的方法执行

`Aspect` 框架定义了很多种切入模式，基本涵盖编程需求，有跳入方法之前之后执行的，环绕方法执行的，抛异常执行的等。

## 二. 开发

### 1. 引入 aop 依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>        
<dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-web</artifactId>
</dependency>

```

### 2. 使用注解切入

- `@Pointcut`： 切入点
- `@Around("aopPoint()")`：环绕切入，可以在方法执行之前之后切入，用于记录请求
- `@AfterThrowing(throwing = "ex", value = "aopPoint()")`：抛出异常之后切入


```java
@Aspect // 当 Spring 检测到这个类带有 @Aspect 注解时将会把这个类设置成 AOP 的类
@Component
public class MethodAop {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("execution(* cn.liweidan.web.service.UserService.*(..))")
    public void aopPoint() {}

    @Around("aopPoint()") // 环绕方法执行注解
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        long start = System.currentTimeMillis();
        logger.info("-----" + className + "." + methodName + "----");
        Object proceed = joinPoint.proceed();
        logger.info("-----" + className + "." + methodName + "End, doing in " + (System.currentTimeMillis() - start) + "ms----");
        return proceed;
    }


    @AfterThrowing(throwing = "ex", value = "aopPoint()") // 方法出现异常时执行
    public void errorAop(JoinPoint joinPoint, Throwable ex) {
        logger.error(ex.toString());
    }

}
```

### 3. 开发一个用户控制器以及用户传输类

```java
@RestController
@RequestMapping("user")
public class UserEndpoint {

    private UserService userService;

    @Autowired
    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    public UserDo findById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

}
public class UserDo {

    private String name;

    private Long id;

    // 省略 getter & setter
}

```

### 4. 开发一个用户业务层

```java
@Service
public class UserService {


    public UserDo findById(Long id) {
        if (id.equals(1L)) {
            throw new ItemNotFoundException("id=" + id + "未找到");
        }

        return new UserDo("Jane", id);
    }

}

```

### 5. 请求访问

根据上面 `AOP` 配置可知，如果请求 `id` 为 `1` 的用户那么将会报错，两个 `AOP` 方法都会被调用到：

```
GET http://localhost:8080/user/1

HTTP/1.1 404 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Thu, 13 Dec 2018 12:59:21 GMT

{
  "code": "404",
  "data": null,
  "errMsg": "未找到id为id=1未找到的资源",
  "success": false
}
```

控制台打印了需要的信息：

```
2018-12-13 20:49:50.041  INFO 1523 --- [nio-8080-exec-1] cn.liweidan.web.common.aop.MethodAop     : -----cn.liweidan.web.service.UserService.findById----
2018-12-13 20:49:50.044 ERROR 1523 --- [nio-8080-exec-1] cn.liweidan.web.common.aop.MethodAop     : cn.liweidan.web.common.ex.ItemNotFoundException: 未找到id为id=1未找到的资源
```

