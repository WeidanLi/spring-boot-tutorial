package cn.liweidan.springboot.jpaddd.dbo;

import javax.persistence.Column;

/**
 * Description：
 *
 * @author liweidan
 * @version 1.0
 * @date 2018-12-19 13:37
 * @email toweidan@126.com
 */
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
