package cn.liweidan.springboot.tomcat.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("hello-springboot")
public class HelloSpringBootEndpoint {

    @GetMapping
    public Map<String, Object> helloSpringBoot() {
        Map<String, Object> resp = new HashMap<>();
        resp.put("Hello", "SpringBoot");
        return resp;
    }

}
