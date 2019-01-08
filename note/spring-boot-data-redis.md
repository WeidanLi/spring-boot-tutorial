# springboot 与 redis 处理缓存

  * [一. 简述](#一-简述)
  * [二. 开发](#二-开发)
    * [（一）连接 redis 数据库](#（一）连接-redis-数据库)
      * [1. 环境准备](#1-环境准备)
      * [2. mvn 依赖](#2-mvn-依赖)
      * [3. 配置 redis 信息](#3-配置-redis-信息)
      * [4. 开发接口以及实体类](#4-开发接口以及实体类)
      * [5. 请求测试](#5-请求测试)
    * [（二）使用 spring 的缓存注解](#（二）使用-spring-的缓存注解)
      * [1. mvn 依赖](#1-mvn-依赖)
      * [2. 启动类配置启动缓存](#2-启动类配置启动缓存)
      * [3. 新增一个缓存注解的控制器和业务层](#3-新增一个缓存注解的控制器和业务层)
      * [4. 测试调用](#4-测试调用)
      * [5. 修改缓存序列化的形式](#5-修改缓存序列化的形式)
      * [6. Cache 的注解](#6-cache-的注解)
        * [0）常用的属性](#0）常用的属性)
        * [1）查询缓存](#1）查询缓存)
        * [2）更新缓存](#2）更新缓存)
        * [3）删除缓存](#3）删除缓存)
        * [4）类全局配置](#4）类全局配置)
      * [7. 注意！！](#7-注意！！)
  * [三. 总结一下](#三-总结一下)


## 一. 简述

`Redis` 是现在大部分项目中使用最多的 `NOSQL` 型数据库，其单线程的模型以及内存级别的读取可以给项目适当加加速。`Redis` 不仅会被当成缓存数据库使用，还会被作为分布式锁（因为是单线程模型）的工具来使用。

`Spring-Boot` 项目有两种方式使用 `Redis` ，接下来就是两种方式的使用方式了。

项目：`spring-boot-data-redis`

## 二. 开发

### （一）连接 redis 数据库

#### 1. 环境准备

使用 `Docker` 启动 `redis` 内存数据库：

```shell
docker run --restart=always -d -p 6379:6379 --name imopei-redis redis redis-server
```

#### 2. mvn 依赖

```xml
<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>

</dependencies>
```

#### 3. 配置 redis 信息

```yaml
spring:
  redis:
    port: 6379 # 端口
    host: localhost # 连接地址
    password: # 密码
    jedis:
      pool:
        max-active: 16   # 最大多少个可用
        max-idle: 8      # 最大存活数量
```

#### 4. 开发接口以及实体类

为了简单，我就只使用接口和自带的 `redisTemplate` 来做数据存储。（当然有些数据真的可以直接存放于 `redis` 数据库）

```java
public class UserDo {

    private String uuid;
    private String name;
    
    // .........
}
@RestController
@RequestMapping("user")
public class UserEndpoint {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("{uid}")
    public UserDo uuidOf(@PathVariable String uid) throws IOException {
        String json = redisTemplate.opsForValue().get(uid);
        UserDo userDo = new ObjectMapper().readValue(json, UserDo.class);
        return userDo;
    }

    @PostMapping
    public void create(@RequestBody UserDo userDo) throws JsonProcessingException {
        redisTemplate.opsForValue().set(userDo.getUuid(), new ObjectMapper().writeValueAsString(userDo));
    }

}
```

#### 5. 请求测试

```
POST http://127.0.0.1:8080/user
Content-Type: application/json

{
  "uuid": "1238910",
  "name": "Weidan"
}

HTTP/1.1 200 
Content-Length: 0
Date: Fri, 04 Jan 2019 08:24:30 GMT

<Response body is empty>

Response code: 200; Time: 202ms; Content length: 0 bytes
---------------------------------------------------------
GET http://127.0.0.1:8080/user/1238910

HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Fri, 04 Jan 2019 08:25:01 GMT

{
  "uuid": "1238910",
  "name": "Weidan"
}

Response code: 200; Time: 89ms; Content length: 34 bytes
```

### （二）使用 spring 的缓存注解

第（一）种方式自由度比较高，可以决定是否要缓存部分数据。

使用了 `spring` 的缓存注解的时候，因为缓存注解是使用 `AOP` 方式切入业务层的，所以可定制度相对就比较低了，一般是直接将整个方法的返回值根据注解定制的 `Key` 进行存储。

不过项目中可以采用两种方式缓和的方式进行开发？可能维护缓存是否有效就比较麻烦了。

我在项目中是采用第一种方式进行存储的。



#### 1. mvn 依赖

需要新增一个包：`spring-boot-starter-cache` 项目

```xml
<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-cache</artifactId>
    </dependency>

</dependencies>
```

#### 2. 启动类配置启动缓存

```java
@SpringBootApplication
@EnableCaching
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

}
```

#### 3. 新增一个缓存注解的控制器和业务层

```java
@RestController
@RequestMapping("usercache")
public class UserCacheEndpoint {

    private UserService userService;

    @Autowired
    public UserCacheEndpoint(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{uid}")
    public UserDo uuidOf(@PathVariable String uid) throws IOException {
        return userService.uuidOf(uid);
    }

    @PostMapping
    public void add(@RequestBody UserDo userDo) throws JsonProcessingException {
        userService.add(userDo);
    }

    @DeleteMapping("{uid}")
    public void del(@PathVariable String uid) {
        userService.deyByUuid(uid);
    }

}

@Service
public class UserService {

    static final String REDIS_VALUE = "user-details";

    // 结果可缓存
    @Cacheable(value = REDIS_VALUE, key = "getArgs()[0]")
    public UserDo uuidOf(String uuid) {
        System.out.println("------");
        UserDo userDo = new UserDo();
        userDo.setUuid(uuid);
        userDo.setName("USER" + uuid);

        return userDo;
    }

    // 删掉缓存
    @CacheEvict(value = REDIS_VALUE, key = "getArgs()[0]")
    public void deyByUuid(String uuid) {
    }

    // 将返回值放入缓存系统中
    @CachePut(value = REDIS_VALUE, key = "getArgs()[0].uuid")
    public UserDo add(UserDo userDo) {
        return userDo;
    }

}

public class UserDo implements Serializable {

    // 需要实现序列化接口和toString！
    
    private String uuid;
    private String name;

    @Override
    public String toString() {
        return "UserDo{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
```

#### 4. 测试调用

```
POST http://127.0.0.1:8080/usercache
Content-Type: application/json

{
  "uuid": "1238910",
  "name": "Weidan"
}

HTTP/1.1 200 
Content-Length: 0
Date: Mon, 07 Jan 2019 09:36:29 GMT

<Response body is empty>

Response code: 200; Time: 127ms; Content length: 0 bytes
```

接下来我需要请求我新增的这个对象，要知道我上面如果没有缓存的话，`get` 出来的 `user` 的名字和这里的名字是不同的。

```java
GET http://127.0.0.1:8080/usercache/1238910

HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 07 Jan 2019 09:37:25 GMT

{
  "uuid": "1238910",
  "name": "Weidan"
}

Response code: 200; Time: 34ms; Content length: 34 bytes
```

OK，已经成功了，实现了我们新增用户的时候就把新增结果存入缓存系统。

接下来我要把它删掉：

```java
DELETE http://127.0.0.1:8080/usercache/1238910

HTTP/1.1 200 
Content-Length: 0
Date: Mon, 07 Jan 2019 09:38:05 GMT

<Response body is empty>

Response code: 200; Time: 25ms; Content length: 0 bytes
```

再重新获取这个 `uid` 的用户

```
GET http://127.0.0.1:8080/usercache/1238910

HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 07 Jan 2019 09:38:28 GMT

{
  "uuid": "1238910",
  "name": "USER1238910"
}

Response code: 200; Time: 30ms; Content length: 39 bytes
```

可以发现，获取的用户信息已经变成我自定义的用户信息了。

到这里缓存系统就已经集成成功了。

接下来，有个需求：

1. 缓存系统缓存的信息在 `redis` 是以二进制的形式存在，不利于查看维护，所以我们需要修改序列化的方式

#### 5. 修改缓存序列化的形式

一般来说，缓存的形式以 `JSON` 的形式存在，当需要手动干预系统的运行（比如某个地方出现 `bug` 暂时不能运行但是又需要删除这个缓存的时候，可以快速定位）可以快速的进行相关的操作。

> 参考资料：[Spring Boot Cache配置 序列化成JSON字符串](https://www.cnblogs.com/cjsblog/p/9150482.html)

相当于只需要在系统中重新定义 `RedisTemplate` 的序列化方式即可，为了方便我没有多余创建配置类，直接写在了启动类里面：

```java
package cn.liweidan.springboot.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Description：启动器
 *
 * @author liweidan
 * @version 1.0
 * @date 2019/1/4 4:14 PM
 * @email toweidan@126.com
 */
@SpringBootApplication
@EnableCaching
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisTemplate redisTemplate) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisTemplate.getConnectionFactory());
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()));
        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
    }

}
```

OK，重新获取上面接口的内容，可以在 `redis` 的桌面端查看数据：

`["cn.liweidan.springboot.redis.dbo.UserDo",{"uuid":"1238910","name":"USER1238910"}]`

#### 6. Cache 的注解

##### 0）常用的属性

缓存的方式相当于我们自己手动设置的 `redisKey` : user-details: ${id}

在 `Redis` 桌面工具查看是会把前缀 user-details 当成一个文件夹来看的，相当于分组

Value: `Redis` 的 `key` 前缀，相当于分组

key: 可变后缀，可以使用常用的函数进行读取，函数调用的方法都存在于 `CacheExpressionRootObject` 类中

##### 1）查询缓存

`@Cacheable(value = REDIS_VALUE, key = "getArgs()[0]")`

这个注解常用于查询，因为运行的机制是先在缓存中查询是否有对应的 `value` 和 `key` ，如果没有再调用方法进行查询。示例中的意思是使用常量 `REDIS_VALUE` 存储前缀，参数的第一个值作为可变后缀。

##### 2）更新缓存

`@CachePut(value = REDIS_VALUE, key = "getArgs()[0].uuid")` 

这个注解常用于更新缓存，无论是新增还是更新操作，只要方法运行完成，会将方法的返回值（注意是方法的返回值，所以更新、新增操作都需要返回对象）存入 `Redis` 中。`value` `key` 用法同上。

##### 3）删除缓存

`@CacheEvict(value = REDIS_VALUE, key = "getArgs()[0]")`

这个注解常用于删除处理，也是方法（一般是删除方法）运行完成后运行，将会把缓存中的数据给删除了。

##### 4）类全局配置

```java
@CacheConfig(cacheNames = {"user-details"})
public class UserService {
}
```

可以定义该类行为下所有的分组 `cacheName` ，这样就不需要在上面三个注解中定义 `value` 值。

#### 7. 注意！！

因为缓存注解 `Spring` 是通过 `AOP` 进行实现的，所以，只有进入 `AOP` 的时候方法才会被缓存，嵌套调用 `service` 方法的时候，缓存注解并不会起作用。

我新增一个接口来做测试，按照上面 `uuidOf` 方法打印的 `------` 做判定

```java
@RestController
@RequestMapping("usercache")
public class UserCacheEndpoint {
    // ......
    @GetMapping("testUuidOfWithMutipleCache/{uid}")
    public UserDo testUuidOfWithMutipleCache(@PathVariable String uid) throws IOException {
        return userService.testUuidOfWithMutipleCache(uid);
    }
}

@Service
@CacheConfig(cacheNames = {"user-details"})
public class UserService {
    // ......
    public UserDo testUuidOfWithMutipleCache(String uid) {
        return uuidOf(uid);
    }
}
```

连续调用三次 `GET http://127.0.0.1:8080/usercache/testUuidOfWithMutipleCache/1238910`

```
....
2019-01-08 09:24:58.154  INFO 8892 --- [on(4)-127.0.0.1] io.lettuce.core.EpollProvider            : Starting without optional epoll library
2019-01-08 09:24:58.155  INFO 8892 --- [on(4)-127.0.0.1] io.lettuce.core.KqueueProvider           : Starting without optional kqueue library
------
------
------
```

可以看到控制台打印了三次，说明缓存并不起作用。

## 三. 总结一下

两种方式各不相同，一种是直接当成数据库调用来看待，一种是通过注解的形式。

当然各有长短板，所以可以说，两种方式可以在项目中结合使用，但是缓存的时候不要互串，比如使用第二种方式创建的缓存在第一种方式获取，后面维护起来可以说很痛苦了。













