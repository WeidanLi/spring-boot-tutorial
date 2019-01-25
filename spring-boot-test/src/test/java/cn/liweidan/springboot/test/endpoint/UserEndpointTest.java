package cn.liweidan.springboot.test.endpoint;

import cn.liweidan.springboot.test.BaseTest;
import cn.liweidan.springboot.test.dbo.UserDo;
import cn.liweidan.springboot.test.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.Assert.assertNotNull;

/**
 * describe: ${describe}
 *
 * @author liweidan
 * @version 1.0
 * @date 2019/1/25 8:17 PM
 * @email toweidan@126.com
 */
public class UserEndpointTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Test
    public void findById() throws Exception {
        UserDo userDo = new UserDo();
        userDo.setUuid("1");
        userDo.setName("Adan");
        userService.create(userDo);

        ObjectMapper mapper = new ObjectMapper();
        mvc.perform(MockMvcRequestBuilders.get("/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> {
                    String contentAsString = mvcResult.getResponse().getContentAsString();
                    UserDo respUserDo = mapper.readValue(contentAsString, UserDo.class);
                    assertNotNull("用户信息不能为空", respUserDo);
                })
                .andDo(MockMvcResultHandlers.print());
    }

}