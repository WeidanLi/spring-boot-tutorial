package cn.liweidan.springboot.data.repository;

import cn.liweidan.springboot.data.dbo.UserDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Description：用户仓库
 *
 * @author liweidan
 * @version 1.0
 * @date 2018-12-19 08:44
 * @email toweidan@126.com
 */
@Repository
public interface UserRepository extends JpaRepository<UserDo, Long> {
}
