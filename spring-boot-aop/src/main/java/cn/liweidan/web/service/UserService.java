package cn.liweidan.web.service;

import cn.liweidan.web.common.ex.ItemNotFoundException;
import cn.liweidan.web.dbo.UserDo;
import org.springframework.stereotype.Service;

/**
 * Description：用户业务层
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/8/30 上午12:38
 * @email toweidan@126.com
 */
@Service
public class UserService {


    public UserDo findById(Long id) {
        if (id.equals(1L)) {
            throw new ItemNotFoundException("id=" + id + "未找到");
        }

        return new UserDo("Jane", id);
    }

}
