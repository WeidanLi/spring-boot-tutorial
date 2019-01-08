package cn.liweidan.springboot.redis.service;

import cn.liweidan.springboot.redis.dbo.UserDo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Description：用户业务层
 *
 * @author liweidan
 * @version 1.0
 * @date 2019/1/4 5:14 PM
 * @email toweidan@126.com
 */
@Service
@CacheConfig(cacheNames = {"user-details"})
public class UserService {

    static final String REDIS_VALUE = "user-details";

    // 结果可缓存
    @Cacheable(key = "getArgs()[0]")
    public UserDo uuidOf(String uuid) {
        System.out.println("------");
        UserDo userDo = new UserDo();
        userDo.setUuid(uuid);
        userDo.setName("USER" + uuid);

        return userDo;
    }

    @CacheEvict(key = "getArgs()[0]")
    public void deyByUuid(String uuid) {
    }

    @CachePut(key = "getArgs()[0].uuid")
    public UserDo add(UserDo userDo) {
        return userDo;
    }

    public UserDo testUuidOfWithMutipleCache(String uid) {
        return uuidOf(uid);
    }
}
