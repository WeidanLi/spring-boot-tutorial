package cn.liweidan.springboot.security.endpoint;

import cn.liweidan.springboot.security.dbo.Resource;
import cn.liweidan.springboot.security.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description：书本控制器
 * @author liweidan
 * @version 1.0
 * @date 2018/11/28 12:07 PM
 * @email toweidan@126.com
 */
@RestController
@RequestMapping("resource")
public class ResourceEndpoint {

    @Autowired
    private ResourceService resourceService;

    @GetMapping("vip")
    public List<Resource> getVipResource() {
        return resourceService.getVipResource();
    }

}
