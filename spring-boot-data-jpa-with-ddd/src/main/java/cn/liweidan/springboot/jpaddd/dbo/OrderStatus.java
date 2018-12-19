package cn.liweidan.springboot.jpaddd.dbo;

import javax.persistence.Column;

/**
 * Description：订单状态
 *
 * @author liweidan
 * @version 1.0
 * @date 2018-12-19 13:53
 * @email toweidan@126.com
 */
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
