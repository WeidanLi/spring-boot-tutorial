package cn.liweidan.springboot.jpaddd.dbo;

/**
 * Description：订单创建事件
 *
 * @author liweidan
 * @version 1.0
 * @date 2018-12-19 14:57
 * @email toweidan@126.com
 */
public class OrderCreated {

    private String orderNo;

    public OrderCreated(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }
}
