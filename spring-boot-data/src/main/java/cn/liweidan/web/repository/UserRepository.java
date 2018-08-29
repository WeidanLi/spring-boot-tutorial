package cn.liweidan.web.repository;

import cn.liweidan.web.dbo.UserDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description：用户数据仓库类
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/8/29 下午7:35
 * @email toweidan@126.com
 */
@Repository
public interface UserRepository extends JpaRepository<UserDo, Long> {
    @Query(value = "SELECT * FROM user_info where user_name=?1", nativeQuery = true)
    List<UserDo> findByName(String name);
}
