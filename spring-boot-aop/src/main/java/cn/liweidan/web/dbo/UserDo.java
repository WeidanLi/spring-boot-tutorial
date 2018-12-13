package cn.liweidan.web.dbo;

/**
 * Description：用户对象
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/8/30 上午12:41
 * @email toweidan@126.com
 */
public class UserDo {

    private String name;

    private Long id;

    public UserDo(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public UserDo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
