package cn.liweidan.springboot.security.dbo;

import javax.persistence.*;

/**
 * Description：用户实体类
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/28 12:30 PM
 * @email toweidan@126.com
 */
@Entity(name = "security_user")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name", length = 13)
    private String name;

    @Column(name = "user_role", length = 20)
    private String role;

    public User() {
    }

    public User(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
