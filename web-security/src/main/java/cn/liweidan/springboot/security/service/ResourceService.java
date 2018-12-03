package cn.liweidan.springboot.security.service;

import cn.liweidan.springboot.security.dbo.Resource;
import cn.liweidan.springboot.security.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description：资源业务层
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/30 10:23 AM
 * @email toweidan@126.com
 */
@Service
public class ResourceService {

    private ResourceRepository resourceRepository;

    @Autowired
    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    /**
     * 只有 vip 的角色才可以访问
     * @return 资源集合
     */
    @PreAuthorize("hasAuthority('vip')")
    public List<Resource> getVipResource() {
        return resourceRepository.findByRequireRole("vip");
    }

}
