package cn.liweidan.springboot.test.service;

import cn.liweidan.springboot.test.dbo.UserDo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Description：用户业务层
 *
 * @author liweidan
 * @version 1.0
 * @date 2019/1/24 8:25 PM
 * @email toweidan@126.com
 */
@Service
public class UserService {

    private Map<String, UserDo> userDB = new HashMap<>();

    public void create(UserDo userDo) {
        if (StringUtils.isEmpty(userDo.getUuid())) {
            throw new IllegalArgumentException("用户uuid不能为空");
        }
        if (StringUtils.isEmpty(userDo.getName())) {
            throw new IllegalArgumentException("用户姓名不能为空");
        }
        userDB.put(userDo.getUuid(), userDo);
    }

    public UserDo findByUID(String UID) {
        UserDo userDo = userDB.get(UID);
        if (Objects.isNull(userDo)) {
            throw new IllegalArgumentException("用户不存在");
        }
        return userDo;
    }

}
