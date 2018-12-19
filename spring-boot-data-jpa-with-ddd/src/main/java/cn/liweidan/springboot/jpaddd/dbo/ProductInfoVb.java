package cn.liweidan.springboot.jpaddd.dbo;

import javax.persistence.Column;

/**
 * Description：产品基本信息
 *
 * @author liweidan
 * @version 1.0
 * @date 2018-12-19 11:51
 * @email toweidan@126.com
 */
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
