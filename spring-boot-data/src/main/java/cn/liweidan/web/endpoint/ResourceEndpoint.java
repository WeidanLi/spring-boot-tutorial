package cn.liweidan.web.endpoint;

import cn.liweidan.web.common.ex.ItemNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 资源控制器
 *
 * @author liweidan
 * @date 2018/8/22 下午3:04
 * @email toweidan@126.com
 */
@RestController
@RequestMapping("resource")
public class ResourceEndpoint {

    @GetMapping("{id}")
    public Map<String, Object> resourceOfId(@PathVariable("id") String id) {

        if (id.equals("100")) {
            throw new ItemNotFoundException(id);
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("id", id);
        resp.put("age", (long) Math.random() * 100);
        resp.put("cardId", UUID.randomUUID().toString());
        return resp;
    }

}
