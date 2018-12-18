package cn.liweidan.springboot.validate.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户数据交换类
 */
public class UserDto {

    @NotNull(groups = Update.class)
    private Integer id;

    @NotBlank(message = "名字不允许为空", groups = Add.class)
    private String userName;

    @NotNull(message = "年龄不允许为空", groups = Add.class)
    private Integer age;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", age=" + age +
                '}';
    }
}
