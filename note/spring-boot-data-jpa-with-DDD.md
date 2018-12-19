# SpringBootJpa 与 DDD 开发

* [SpringBootJpa 与 DDD 开发](#springbootjpa-与-ddd-开发)
  * [一. 简述](#一-简述)
  * [二. 开发](#二-开发)
    * [1. 设定情景](#1-设定情景)
    * [2. mvn依赖](#2-mvn依赖)
    * [2. 设计订单Entity](#2-设计订单entity)
    * [3. 订单仓库](#3-订单仓库)
    * [4. 订单业务层和产品业务层](#4-订单业务层和产品业务层)
    * [5. 订单接口](#5-订单接口)
    * [6. 接口测试](#6-接口测试)
    * [7. 仓库事件通知](#7-仓库事件通知)
      * [7.1 在Entity新增要发布的事件](#71-在entity新增要发布的事件)
      * [7.2 业务层监听事件：](#72-业务层监听事件：)
      * [7.3 调用测试](#73-调用测试)
  * [三. 总结](#三-总结)

## 一. 简述

`DDD` 开发，相信开发几年的工程师一般都会知道这个名词了。还原面向对象的本质，用代码诠释业务。由 `Eric Evans` 提出但是响应起来并不是很简单。

知道 `DDD` 的都知道，使用集合型 `orm` 框架来开发会更加觉得顺手，`mybatis` 就不大合适了。所以 `Spring` 公司对 `jpa` 加入了一些支持。

`DDD` 几句话并不能讲清楚，这里就只是提供简单 `Demo` 和思路。

**示例代码：**

## 二. 开发

### 1. 设定情景

首先假定一个订单只能下单一个产品。（如果需要多产品的，需要设计订单主体信息和订单详情信息）

首先设定要开发一个下单的接口，我们下单的时候，订单都会存储一些产品的基本信息。

那么这时候一个订单就是一个实体 `Entity` （与 `jpa` 的注解不谋而合），产品信息就是一个 `ValueObject`（`ValueObject` 一般没有无参构造器，也没有 `setter`，修改信息都需要进行整体替换） ，订单一旦生成就不能修改产品的基本信息，所以订单实体不能有产品信息的 `setter`。

OK，业务需求出来了，那么设计订单的时候一般在数据库的表现是订单和产品信息放在一起。

### 2. mvn依赖

跟简单实用的示例一样。

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

    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### 2. 设计订单Entity

订单实体一般有主体信息，订单状态，购买信息以及订单状态。所以我设计了一个聚合还有三个 `ValueObject`

订单实体：

```java
@Entity
@Table(name = "order_db")
public class OrderEntity {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no",length = 100)
    private String orderNo;

    @Column(name = "order_total_amount")
    private Long totalAccount;// 总价格 单位分

    @Embedded // 指定这个类是同个数据库表，但是把属性抽取到同一个对象里面去
    private ProductInfoVb productInfo;

    @Embedded
    private BuyInfoVb buyInfo;

    @Embedded
    private OrderStatus orderStatus;

    /**
     * 带参构造器
     * @param orderNo 订单号
     * @param productInfo 产品信息
     */
    public OrderEntity(String orderNo, ProductInfoVb productInfo, BuyInfoVb buyInfo) {
        this.orderNo = orderNo;
        this.productInfo = productInfo;
        this.totalAccount = buyInfo.getQuantity() * productInfo.getPrice();
        this.orderStatus = new OrderStatus("WAIT");
    }

    public OrderEntity(String orderNo, ProductInfoVb productInfo, long prodId, long quantity) {
        this.orderNo = orderNo;
        this.productInfo = productInfo;
        this.buyInfo = new BuyInfoVb(prodId, quantity);
        this.totalAccount = buyInfo.getQuantity() * productInfo.getPrice();
    }

    /**
     * 无参构造器，以便 hibernate 可以创建实体类
     */
    protected OrderEntity(){}

    public Long getId() {
        return id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public Long getTotalAccount() {
        return totalAccount;
    }

    public ProductInfoVb getProductInfo() {
        return productInfo;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
```

三个 `value object`

```java
public class ProductInfoVb {

    @Column(name = "order_product_name", length = 150)
    private String prodName;

    @Column(name = "order_product_price")
    private Long price;

    public ProductInfoVb(String prodName, Long price) {
        this.prodName = prodName;
        this.price = price;
    }

    protected ProductInfoVb(){}

    public String getProdName() {
        return prodName;
    }

    public Long getPrice() {
        return price;
    }
}
public class OrderStatus {

    @Column(name = "order_status")
    private String orderStatus;

    public OrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    protected OrderStatus() {
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
public class BuyInfoVb {

    @Column(name = "order_product_id")
    private Long prodId;

    @Column(name = "order_product_quantity")
    private Long quantity;

    public BuyInfoVb(Long prodId, Long quantity) {
        this.prodId = prodId;
        this.quantity = quantity;
    }

    protected BuyInfoVb(){}

    public Long getProdId() {
        return prodId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
```

### 3. 订单仓库

```java
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {}
```

### 4. 订单业务层和产品业务层

```java
@Service
@Transactional
public class OrderService {

    private ProductService productService;
    private OrderRepository orderRepository;

    @Autowired
    public OrderService(ProductService productService,
                        OrderRepository orderRepository) {
        this.productService = productService;
        this.orderRepository = orderRepository;
    }

    public OrderEntity create(Long prodId, Long quantity) {
        ProductInfoVb productInfoVb = productService.idOf(prodId);
        OrderEntity orderEntity = new OrderEntity(UUID.randomUUID().toString(), productInfoVb, prodId, quantity);
        orderRepository.save(orderEntity);
        return orderEntity;
    }

    public List<OrderEntity> all() {
        return orderRepository.findAll();
    }

    public void payOrder(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id).orElse(null);
        orderEntity.setOrderStatus(new OrderStatus("PAY_SUCCESS"));
    }
}
```

```java
// 其实产品业务层基本上是连接第三方服务获取信息，
// 封装成本服务所需要的对象
// 是工厂类和防污层
@Service
public class ProductService {

    public ProductInfoVb idOf(Long id) {
        return new ProductInfoVb("iMac", 12000L);
    }

}
```

### 5. 订单接口

```java
@RestController
public class OrderEndpoint {

    private OrderService orderService;

    @Autowired
    public OrderEndpoint(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public void createOrder(@RequestParam("prodId") Long prodId, @RequestParam("quantity") Long quantity) {
        orderService.create(prodId, quantity);
    }

    @GetMapping
    public List<OrderEntity> all() {
        return orderService.all();
    }

    @PostMapping("{id}")
    public void payOrder(@PathVariable("id") Long id) {
        orderService.payOrder(id);
    }

}
```

### 6. 接口测试

```
# 创建一个订单
POST http://127.0.0.1:8080?prodId=100&quantity=2

HTTP/1.1 200 
Content-Length: 0
Date: Wed, 19 Dec 2018 06:18:49 GMT

<Response body is empty>

Response code: 200; Time: 238ms; Content length: 0 bytes
-------------------------------------------------------
# 查询所有订单
GET http://127.0.0.1:8080

HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Wed, 19 Dec 2018 06:19:48 GMT

[
  {
    "id": 1,
    "orderNo": "9a96a1f9-88a7-4d64-a816-42ea57de09ab",
    "totalAccount": 24000,
    "productInfo": {
      "prodName": "iMac",
      "price": 12000
    },
    "orderStatus": {
      "orderStatus": "WAIT"
    }
  }
]

Response code: 200; Time: 135ms; Content length: 165 bytes
-------------------------------------------------------
# 支付订单
POST http://127.0.0.1:8080/1/

HTTP/1.1 200 
Content-Length: 0
Date: Wed, 19 Dec 2018 06:20:25 GMT

<Response body is empty>

Response code: 200; Time: 78ms; Content length: 0 bytes
-------------------------------------------------------
# 重新查询所有订单
GET http://127.0.0.1:8080

HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Wed, 19 Dec 2018 06:20:48 GMT

[
  {
    "id": 1,
    "orderNo": "9a96a1f9-88a7-4d64-a816-42ea57de09ab",
    "totalAccount": 24000,
    "productInfo": {
      "prodName": "iMac",
      "price": 12000
    },
    "orderStatus": {
      "orderStatus": "PAY_SUCCESS"
    }
  }
]

Response code: 200; Time: 50ms; Content length: 172 bytes
```

### 7. 仓库事件通知

一般我们使用 `Repository` 都是使用在聚合上，在 `DDD` 模型中，当聚合发生变化的时候，都会发送事件。

所以我们可以在 `Entity` 类上使用 `@DomainEvents` 和 `@AfterDomainEventPublication` 来发送事件。

但是这里有个问题是，当我们更新 `Entity` 的时候，也需要去调用 `save()` 方法，不调用不会发送事件。

所以，也许某些业务自己手动发布事件会更好，简单的增删查改就可以使用这个。

#### 7.1 在Entity新增要发布的事件

```java
@Entity
@Table(name = "order_db")
public class OrderEntity {

    // 省略...

    @DomainEvents // 发布事件
    Collection<Object> domainEvents() {
        List<Object> orderCreateds = new ArrayList<>(1);
        if (orderStatus.getOrderStatus().equals("WAIT")) {
            orderCreateds.add(new OrderCreated(orderNo));
        } else if (orderStatus.getOrderStatus().equals("PAY_SUCCESS")) {
            orderCreateds.add(new OrderPaied(orderNo));
        }
        return orderCreateds;
    }

    @AfterDomainEventPublication // 发布完事件进行清理(如果需要)
    void callbackMethod() {
        System.out.println("EventsPublished...");
    }
}
```

事件对象（写的比较简单）：

```java
public class OrderCreated {

    private String orderNo;

    public OrderCreated(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }
}
```

#### 7.2 业务层监听事件：

```java
@Service
@Transactional
public class OrderService {
	/**
     * 监听不同事件，Spring 会根据事件对象去调用相应的方法
     * 如果事件跟当前业务无太大关系(比如统计什么的） 这个方法可以设置 @Async
     * @param orderCreated
     */
    @TransactionalEventListener
    public void orderCreated(OrderCreated orderCreated) {
        System.out.println("订单被创建, 单号: " + orderCreated.getOrderNo());
    }

    @TransactionalEventListener
    public void orderPaied(OrderPaied orderPaied) {
        System.out.println("订单被支付, 单号: " + orderPaied.getOrderNo());
    }

}
```

#### 7.3 调用测试

```
...
2018-12-19 15:12:42.835  INFO 60193 --- [on(2)-127.0.0.1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 7 ms
Hibernate: insert into order_db (order_product_id, order_product_quantity, order_no, order_status, order_product_price, order_product_name, order_total_amount) values (?, ?, ?, ?, ?, ?, ?)
EventsPublished...
订单被创建, 单号: a5daff61-a270-407d-a3f3-623af061669d
Hibernate: select orderentit0_.order_id as order_id1_0_0_, orderentit0_.order_product_id as order_pr2_0_0_, orderentit0_.order_product_quantity as order_pr3_0_0_, orderentit0_.order_no as order_no4_0_0_, orderentit0_.order_status as order_st5_0_0_, orderentit0_.order_product_price as order_pr6_0_0_, orderentit0_.order_product_name as order_pr7_0_0_, orderentit0_.order_total_amount as order_to8_0_0_ from order_db orderentit0_ where orderentit0_.order_id=?
EventsPublished...
Hibernate: update order_db set order_product_id=?, order_product_quantity=?, order_no=?, order_status=?, order_product_price=?, order_product_name=?, order_total_amount=? where order_id=?
订单被支付, 单号: a5daff61-a270-407d-a3f3-623af061669d
```

## 三. 总结

1. 每个更新都需要手动调用 `save` 才会发布事件
2. 针对的业务实体，该事件发布显得无力















