package cn.liweidan.springboot.jpaddd.repository;

import cn.liweidan.springboot.jpaddd.dbo.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Description：订单仓库
 *
 * @author liweidan
 * @version 1.0
 * @date 2018-12-19 13:35
 * @email toweidan@126.com
 */
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
