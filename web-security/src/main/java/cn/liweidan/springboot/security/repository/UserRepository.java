package cn.liweidan.springboot.security.repository;

import cn.liweidan.springboot.security.dbo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Description：用户仓库
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/28 12:43 PM
 * @email toweidan@126.com
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);

}
