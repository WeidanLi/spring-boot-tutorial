package cn.liweidan.springboot.security.conf;

import cn.liweidan.springboot.security.dbo.Resource;
import cn.liweidan.springboot.security.dbo.User;
import cn.liweidan.springboot.security.repository.ResourceRepository;
import cn.liweidan.springboot.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Description：
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/28 12:45 PM
 * @email toweidan@126.com
 */
@Configuration
public class InitDbConfig {

    private UserRepository userRepository;
    private ResourceRepository resourceRepository;

    @Autowired
    public InitDbConfig(UserRepository userRepository, ResourceRepository resourceRepository) {
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
    }

    @PostConstruct
    public void initUser() {
        User user1 = new User("WEIDAN", "admin");
        User user2 = new User("XIAOMING", "vip");
        User user3 = new User("XIAOHONG", "customer");
        List<User> addUser = new ArrayList<>(3);
        addUser.add(user1);
        addUser.add(user2);
        addUser.add(user3);
        userRepository.saveAll(addUser);
    }

    @PostConstruct
    public void initResource() {
        Resource resource1 = new Resource("admin", UUID.randomUUID().toString());
        Resource resource2 = new Resource("admin", UUID.randomUUID().toString());
        Resource resource3 = new Resource("vip", UUID.randomUUID().toString());
        Resource resource4 = new Resource("admin", UUID.randomUUID().toString());
        Resource resource5 = new Resource("vip", UUID.randomUUID().toString());
        Resource resource6 = new Resource("admin", UUID.randomUUID().toString());
        Resource resource7 = new Resource("vip", UUID.randomUUID().toString());
        Resource resource8 = new Resource("admin", UUID.randomUUID().toString());
        Resource resource9 = new Resource("admin", UUID.randomUUID().toString());
        Resource resource10 = new Resource("customer", UUID.randomUUID().toString());
        List<Resource> resourceList = new ArrayList<>(10);
        resourceList.add(resource1);
        resourceList.add(resource2);
        resourceList.add(resource3);
        resourceList.add(resource4);
        resourceList.add(resource5);
        resourceList.add(resource6);
        resourceList.add(resource7);
        resourceList.add(resource8);
        resourceList.add(resource9);
        resourceList.add(resource10);
        resourceRepository.saveAll(resourceList);
    }

}
