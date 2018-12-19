package cn.liweidan.springboot.jpaddd.service;

import cn.liweidan.springboot.jpaddd.dbo.ProductInfoVb;
import org.springframework.stereotype.Service;

/**
 * Description：产品业务层
 *
 * @author liweidan
 * @version 1.0
 * @date 2018-12-19 13:37
 * @email toweidan@126.com
 */
@Service
public class ProductService {

    public ProductInfoVb idOf(Long id) {
        return new ProductInfoVb("iMac", 12000L);
    }

}
