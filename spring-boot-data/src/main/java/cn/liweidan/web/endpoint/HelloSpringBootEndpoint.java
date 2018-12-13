package cn.liweidan.web.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 第一个控制器
 *
 * @author liweidan
 * @date 2018/8/22 下午2:42
 * @email toweidan@126.com
 */
@RestController // #1
@RequestMapping("hello-springboot") // #2
public class HelloSpringBootEndpoint {

    @GetMapping // #3
    public Map<String, Object> helloSpringBoot() {
        Map<String, Object> resp = new HashMap<>();
        resp.put("Hello", "SpringBoot");
        return resp;
    }

}
