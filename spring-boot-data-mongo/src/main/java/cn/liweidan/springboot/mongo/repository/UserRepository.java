package cn.liweidan.springboot.mongo.repository;

import cn.liweidan.springboot.mongo.dbo.UserDo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Description：用户仓库
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/12/28 9:55 AM
 * @email toweidan@126.com
 */
@Repository
public interface UserRepository extends MongoRepository<UserDo, String> { // 继承 跟 JpaRepository 一样的用法
}
