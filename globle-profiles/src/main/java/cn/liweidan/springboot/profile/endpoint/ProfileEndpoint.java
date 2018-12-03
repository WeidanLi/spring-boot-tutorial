package cn.liweidan.springboot.profile.endpoint;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Description：环境控制器
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/12/1 10:53 AM
 * @email toweidan@126.com
 */
@RestController
@RequestMapping
public class ProfileEndpoint {

    @Value("${common.profile}")
    private String common;
    @Value("${diff.profile}")
    private String diffProfile;

    @GetMapping
    public Map<String, Object> loadProfiles() {
        Map<String, Object> profileMap = new HashMap<>(2);
        profileMap.put("common", common);
        profileMap.put("diffProfile", diffProfile);
        return profileMap;
    }

}
