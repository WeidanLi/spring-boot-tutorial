package cn.liweidan.springboot.jpaddd.service;

import cn.liweidan.springboot.jpaddd.dbo.*;
import cn.liweidan.springboot.jpaddd.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.UUID;

/**
 * Description：订单业务层
 *
 * @author liweidan
 * @version 1.0
 * @date 2018-12-19 13:37
 * @email toweidan@126.com
 */
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
        orderRepository.save(orderEntity);
    }

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
