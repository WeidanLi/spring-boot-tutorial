package cn.liweidan.springboot.excepresp.dto;

/**
 * Description：用户类
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/20 12:21 AM
 * @email toweidan@126.com
 */
public class UserDto {

    private Long id;
    private String name;
    private Integer age;

    public UserDto(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public UserDto() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }
}
