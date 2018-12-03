package cn.liweidan.springboot.swagger2.dto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description：用户DO类
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/22 11:01 AM
 * @email toweidan@126.com
 */
@ApiModel // 指定该 dto 类是一个数据传递的类
public class UserDto {

    // 指定属性的名字和说明
    @ApiModelProperty(name = "id", notes = "用户存储的唯一ID")
    private Long id;
    @ApiModelProperty(name = "name", notes = "用户名字")
    private String name;
    @ApiModelProperty(name = "salary", notes = "用户的薪资数字，单位：分")
    private Long salary;
    @ApiModelProperty(name = "age", notes = "用户的年龄")
    private Integer age;

    public UserDto(Long id, String name, Long salary, Integer age) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.age = age;
    }

    public UserDto() {
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
