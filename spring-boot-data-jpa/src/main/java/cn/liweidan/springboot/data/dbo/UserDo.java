package cn.liweidan.springboot.data.dbo;

import javax.persistence.*;

/**
 * Description：用户数据库映射类
 *
 * @author liweidan
 * @version 1.0
 * @date 2018-12-19 08:39
 * @email toweidan@126.com
 */
@Entity // 指定这是一个数据库映射类
@Table(name = "user_db") // 表的基本信息
public class UserDo {

    @Id // 规定这是一个主键
    @Column(name = "user_id") // column 的定义
    @GeneratedValue(strategy= GenerationType.IDENTITY) // 使用数据库 id 自增的功能
    private Long id;

    @Column(name = "user_name", length = 50)
    private String name;

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
