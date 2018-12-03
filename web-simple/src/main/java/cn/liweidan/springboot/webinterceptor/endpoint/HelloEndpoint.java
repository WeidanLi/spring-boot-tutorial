package cn.liweidan.springboot.webinterceptor.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Description：HelloWorld 控制器
 * @author liweidan
 * @version 1.0
 * @date 2018/11/19 12:14 PM
 * @email toweidan@126.com
 */
@RestController // 使用 RestController，指定该控制器输出都是 json 对象
public class HelloEndpoint {

    @GetMapping // 使用 GetMapping 代替 RequestMapping、同理还有 PostMapping PutMapping DeleteMapping
    public Map<String, String> helloWorld() {
        Map<String, String> helloMap = new HashMap<>(1);
        helloMap.put("hello", "world");
        return helloMap;
    }

}
