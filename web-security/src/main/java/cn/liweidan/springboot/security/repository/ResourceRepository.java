package cn.liweidan.springboot.security.repository;

import cn.liweidan.springboot.security.dbo.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description：资源仓库
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/30 11:28 AM
 * @email toweidan@126.com
 */
@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    List<Resource> findByRequireRole(String requireRols);

}
