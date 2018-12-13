package cn.liweidan.web.dbo;

import javax.persistence.*;

/**
 * Description：用户
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/8/29 下午7:21
 * @email toweidan@126.com
 */
@Entity
@Table(name = "user_info")
public class UserDo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name", length = 50)
    private String name;

    @Column(name = "user_age", length = 50)
    private Integer age;

    public UserDo() {
    }

    public UserDo(String name, Integer age) {
        this.name = name;
        this.age = age;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
