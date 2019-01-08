package cn.liweidan.springboot.redis.endpoint;

import cn.liweidan.springboot.redis.dbo.UserDo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Description：用户接口层
 *
 * @author liweidan
 * @version 1.0
 * @date 2019/1/4 4:10 PM
 * @email toweidan@126.com
 */
@RestController
@RequestMapping("user")
public class UserEndpoint {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("{uid}")
    public UserDo uuidOf(@PathVariable String uid) throws IOException {
        String json = redisTemplate.opsForValue().get(uid);
        UserDo userDo = new ObjectMapper().readValue(json, UserDo.class);
        return userDo;
    }

    @PostMapping
    public void create(@RequestBody UserDo userDo) throws JsonProcessingException {
        redisTemplate.opsForValue().set(userDo.getUuid(), new ObjectMapper().writeValueAsString(userDo));
    }

}
