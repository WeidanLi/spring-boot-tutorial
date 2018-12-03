package cn.liweidan.springboot.security.dbo;

import javax.persistence.*;

/**
 * Description：书本实体类
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/28 12:21 PM
 * @email toweidan@126.com
 */
@Entity(name = "security_resource")
public class Resource {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "res_id")
    private Long id;

    @Column(name = "res_role", length = 20)
    private String requireRole;

    @Column(name = "res_content")
    private String content;

    public Resource() {
    }

    public Resource(String requireRole, String content) {
        this.requireRole = requireRole;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getRequireRole() {
        return requireRole;
    }

    public String getContent() {
        return content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRequireRole(String requireRole) {
        this.requireRole = requireRole;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
