package cn.liweidan.springboot.mybatis.dbo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Description：用户数据库映射类
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/12/27 5:59 PM
 * @email toweidan@126.com
 */
@Table(name = "user_db")
public class UserDo {

    @Column(name = "user_uuid")
    @Id
    private String uuid;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_id")
    private String id;

    @Column(name = "user_salary")
    private Long salary;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }
}
