package cn.liweidan.springboot.webrestful.dbo;

/**
 * Description：用户DO类
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/22 11:01 AM
 * @email toweidan@126.com
 */
public class UserDo {

    private Long id;
    private String name;
    private Long salary;
    private Integer age;

    public UserDo(Long id, String name, Long salary, Integer age) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.age = age;
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

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}
