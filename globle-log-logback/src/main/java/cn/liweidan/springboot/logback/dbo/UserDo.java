package cn.liweidan.springboot.logback.dbo;

/**
 * Description：用户类
 *
 * @author liweidan
 * @version 1.0
 * @date 2018-12-03 17:40
 * @email toweidan@126.com
 */
public class UserDo {

    private Long id;
    private String name;

    public UserDo(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserDo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
