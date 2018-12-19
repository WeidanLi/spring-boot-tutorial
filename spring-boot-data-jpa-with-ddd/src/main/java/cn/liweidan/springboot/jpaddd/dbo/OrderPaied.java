package cn.liweidan.springboot.jpaddd.dbo;

/**
 * Description：订单支付事件
 *
 * @author liweidan
 * @version 1.0
 * @date 2018-12-19 14:57
 * @email toweidan@126.com
 */
public class OrderPaied {

    private String orderNo;

    public OrderPaied(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }
}
