package cn.liweidan.springboot.jpaddd.endpoint;

import cn.liweidan.springboot.jpaddd.dbo.OrderEntity;
import cn.liweidan.springboot.jpaddd.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description：订单接口
 *
 * @author liweidan
 * @version 1.0
 * @date 2018-12-19 13:36
 * @email toweidan@126.com
 */
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
