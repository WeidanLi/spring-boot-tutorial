package cn.liweidan.springboot.mybatis.mapper;

import cn.liweidan.springboot.mybatis.dbo.UserDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description：用户Mapper层
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/12/27 6:02 PM
 * @email toweidan@126.com
 */
@Mapper
public interface UserMapper extends tk.mybatis.mapper.common.Mapper<UserDo> {

    UserDo selectUserById(@Param("userUuid") String uuid);

    int addUser(UserDo userDo);

    List<UserDo> selectAllUser();

}
