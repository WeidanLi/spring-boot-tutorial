package cn.liweidan.springboot.jpaddd.dbo;

import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Description：订单实体类
 *
 * @author liweidan
 * @version 1.0
 * @date 2018-12-19 11:49
 * @email toweidan@126.com
 */
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
        this.orderStatus = new OrderStatus("WAIT");
    }

    @DomainEvents
    Collection<Object> domainEvents() {
        List<Object> orderCreateds = new ArrayList<>(1);
        if (orderStatus.getOrderStatus().equals("WAIT")) {
            orderCreateds.add(new OrderCreated(orderNo));
        } else if (orderStatus.getOrderStatus().equals("PAY_SUCCESS")) {
            orderCreateds.add(new OrderPaied(orderNo));
        }
        return orderCreateds;
    }

    @AfterDomainEventPublication
    void callbackMethod() {
        System.out.println("EventsPublished...");
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
