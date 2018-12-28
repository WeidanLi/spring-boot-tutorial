# springboot 与 MongoDB数据处理

* [springboot 与 MongoDB数据处理](#springboot-与-mongodb数据处理)
  * [一. 简述](#一-简述)
  * [二. 开发](#二-开发)
    * [（一）MongoDB 环境的准备](#（一）mongodb-环境的准备)
        * [1. 使用 Docker 启动 MongoDB 环境](#1-使用-docker-启动-mongodb-环境)
* [启动MongoDB](#启动mongodb)
* [启动MongoDB管理后台](#启动mongodb管理后台)
        * [2. 启动](#2-启动)
    * [（二）项目开发](#（二）项目开发)
      * [1. mvn 依赖](#1-mvn-依赖)
      * [2. 编写 UserDo 实体类](#2-编写-userdo-实体类)
      * [3. 编写 MongoDB 的数据仓库](#3-编写-mongodb-的数据仓库)
      * [4. 调用](#4-调用)
      * [5. 测试调用](#5-测试调用)
        * [1. 先新增两条记录](#1-先新增两条记录)
        * [2. 查询所有用户信息](#2-查询所有用户信息)
        * [3. 查询单条记录](#3-查询单条记录)
  * [三. 总结](#三-总结)



## 一. 简述

`MongoDB` 是一款新兴的 `NO-SQL` 数据库，使用的是类 `JSON` 的数据格式 `BSON` 进行数据存储。`MongoDB` 将索引放在内存中，因此查询的时候会大幅度提高速度。在数据库领域中，`MongoDB` 适合单表的或者说 `Column` 不需要高度统一的业务处理中，比如：用户娱乐信息表（在商城的用户信息中存储的用户兴趣、用户QQ等等）。`MongoDB` 对事务支持度不算太高，日常需求需要连表操作的就不要放在 `MongoDB` 了。

`SpringBoot` 对 `MongoDB` 提供了 `starter` 让 `Javaer` 能够更加简单的操作 `MongoDB` 数据库的信息。

项目演示：`spring-boot-data-mongo`

## 二. 开发

### （一）MongoDB 环境的准备

##### 1. 使用 Docker 启动 MongoDB 环境

```yaml
version: '3.1'

services:
# 启动MongoDB
  mongo:
    image: mongo
    restart: always
    ports:
      - 27017:27017
# 启动MongoDB管理后台
  mongo-express:
    image: mongo-express
    restart: always
    ports:
      - 8081:8081
```

##### 2. 启动

```yaml
docker-compose -f mongodb.yml up
```

访问 `http://localhost:8081` 如果可以看到管理页面即为成功

### （二）项目开发

#### 1. mvn 依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!-- MongoDB starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependencies>
```

#### 2. 编写 UserDo 实体类

```java
@Document(collection = "user_info") // 指定这个类是一个Document，类似于@Entity，可以在注解中指定 collection (MySQL 中的 Table)
public class UserDo {

    @Id
    private String uuid;

    private String fristName;

    private String lastName;

    public UserDo() {
    }

    // 省略 setter 和 getter
}
```

#### 3. 编写 MongoDB 的数据仓库

```java
@Repository
public interface UserRepository extends MongoRepository<UserDo, String> { // 继承 跟 JpaRepository 一样的用法
}
```

#### 4. 调用

为了方便我省略了业务层直接在接口层进行调用：

```java
@RestController
@RequestMapping("user")
public class UserEndpoint {

    private UserRepository userRepository;

    @Autowired
    public UserEndpoint(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<UserDo> findAll() {
        return userRepository.findAll();
    }

    @PostMapping
    public void addUser(@RequestBody UserDo userDo) {
        userDo.setUuid(UUID.randomUUID().toString());
        userRepository.save(userDo);
    }

    @GetMapping("{uid}")
    public UserDo findByUID(@PathVariable("uid") String uid) {
        return userRepository.findById(uid).get();
    }

}
```

#### 5. 测试调用

##### 1. 先新增两条记录

```
POST http://127.0.0.1:8080/user
Content-Type: application/json

{
  "fristName": "MING",
  "lastName": "LI"
}

HTTP/1.1 200 
Content-Length: 0
Date: Fri, 28 Dec 2018 03:05:59 GMT

<Response body is empty>

Response code: 200; Time: 259ms; Content length: 0 bytes
--------------------------------------------------------
POST http://127.0.0.1:8080/user
Content-Type: application/json

{
  "fristName": "WEIDAN",
  "lastName": "LI"
}

HTTP/1.1 200 
Content-Length: 0
Date: Fri, 28 Dec 2018 03:05:59 GMT

<Response body is empty>

Response code: 200; Time: 259ms; Content length: 0 bytes

```

##### 2. 查询所有用户信息

```
GET http://127.0.0.1:8080/user

HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Fri, 28 Dec 2018 03:11:16 GMT

[
  {
    "uuid": "5354b97f-bc1f-4198-97f1-d10ee624493e",
    "fristName": "MING",
    "lastName": "LI"
  },
  {
    "uuid": "23916e7c-cfa2-4ca8-9c8a-826c7abd6c9d",
    "fristName": "WEIDAN",
    "lastName": "LI"
  }
]

Response code: 200; Time: 26ms; Content length: 169 bytes
```

##### 3. 查询单条记录

```
GET http://127.0.0.1:8080/user/5354b97f-bc1f-4198-97f1-d10ee624493e

HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Fri, 28 Dec 2018 03:11:50 GMT

{
  "uuid": "5354b97f-bc1f-4198-97f1-d10ee624493e",
  "fristName": "MING",
  "lastName": "LI"
}

Response code: 200; Time: 171ms; Content length: 82 bytes
```

## 三. 总结

本文演示了如何使用 `spring-boot` 操作 `MongoDB` 数据库，刚开始因为设置了用户名和密码，需要授权访问不上（原谅我第一次使用，不知道怎么授权= =）查询官方文档说是鼓励在内网完全开放，让任何项目可以随时连接上去（比如用 `Docker` 进行容器连接访问），不过不设置密码估计都过不了自己心里那一关，所以还是设置用户名密码，根据要求在 `MongoDB` 中进行授权。

























