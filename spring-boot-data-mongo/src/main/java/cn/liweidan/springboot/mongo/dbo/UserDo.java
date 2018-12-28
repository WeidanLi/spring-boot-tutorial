package cn.liweidan.springboot.mongo.dbo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Description：用户实体类
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/12/28 9:53 AM
 * @email toweidan@126.com
 */
@Document(collection = "user_info") // 指定这个类是一个Document，类似于@Entity，可以在注解中指定 collection (MySQL 中的 Table)
public class UserDo {

    @Id
    private String uuid;

    private String fristName;

    private String lastName;

    public UserDo() {
    }

    public UserDo(String uuid, String fristName, String lastName) {
        this.uuid = uuid;
        this.fristName = fristName;
        this.lastName = lastName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFristName() {
        return fristName;
    }

    public void setFristName(String fristName) {
        this.fristName = fristName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
