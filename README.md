即使现在网上关于 `spring-boot` 的相关教程很多，很多up主也都写得很棒，基本符合开发需求。但是我还是写写我自己对 `spring-boot` 的理解。

##  一. `spring-boot` 介绍

相信很多拥有几年开发经验的程序员来说，`spring` 项目并不陌生，他是一个在 `java` 这么语言中一个很重要的 `ioc` 框架，将代码利用可配置的方式，织入到项目中。`spring` 项目拥有很多模块，但是最核心的模块莫过于 `beans` `context` `aop` 了。后面的模块比如事务管理器，也是通过 `aop` 项目进行数据库事务的管理。

由于实际开发过程中，我们经常需要整合第三方的中间件，诸如国内常用配置的 `redis` `druid` `rabbitMQ` 等等。这会导致 `spring` 相关配置在项目中呈现爆炸式的增长。

`spring` 公司意识到这个问题，故开发了 `spring-boot` 项目进行配置上的简化。在我看来 `spring-boot` 项目主要解决两大问题：

1. 提供一系列的 `starter` 快速整合第三方框架，提供 `starter` 开发接口，供中间件开发者们进行适配 `spring` 项目，在使用者看来，就是依赖一个第三方的 `starter` ，在 `yml` 配置中增加特定前缀的配置即可，大大减少配置复杂性；
2. 还是依靠 `starter` ，其项目的 `pom.xml` 文件会将可兼容的第三方版本框架依赖到开发项目中。

所以说，很多人常常说 `spring-boot` 是个轻量级的框架。其实这种说法是错误的，因为并不是依赖了 `spring-boot` 就不需要 `spring` 其他模块了。同理，通过 `starter` 需要依赖的还是会跟进来。只是开发流程变得轻量级，仅此而已。

## 二. `spring-boot` 常用套装

`spring-boot` 使用起来，也有常规的套路，这里我将我在实际生产中使用的套装，给剖析出来。

1. [`spring-boot` 的 `web` 开发项目](spring-boot-web.MD) （项目：`spring-boot-web`）
2. `spring-boot` 整合 `mybatis` 进行数据库访问
3. `spring-boot` 使用 `jpa` 进行数据库访问
4. `spring-boot` 使用 `redis` 进行数据存储
5. `spring-boot` 使用 `druid` 作为数据库连接池
6. `spring-boot` 通过 `aop` 进行日志收集
7. `spring-boot` 配置健康监控节点
8. `spring-boot` 打包运行
9. `spring-boot` 在 `Tomcat` 中运行