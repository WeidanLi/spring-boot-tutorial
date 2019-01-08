package cn.liweidan.springboot.redis.dbo;

import java.io.Serializable;

/**
 * Description：用户实体类
 *
 * @author liweidan
 * @version 1.0
 * @date 2019/1/4 4:12 PM
 * @email toweidan@126.com
 */
public class UserDo implements Serializable {

    private String uuid;
    private String name;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserDo{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
