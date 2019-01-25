package cn.liweidan.springboot.test.dbo;

/**
 * Description：用户实体类
 *
 * @author liweidan
 * @version 1.0
 * @date 2019/1/24 8:25 PM
 * @email toweidan@126.com
 */
public class UserDo {

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
}
