package cn.liweidan.springboot.swagger2.endpoint;

import cn.liweidan.springboot.swagger2.dto.UserDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description：用户资源控制器
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/22 11:00 AM
 * @email toweidan@126.com
 */
@RestController
@RequestMapping("user")
public class UserEndpoint {

    /** 公司关联用户ID表 */
    Map<Long, List<Long>> orgRelationUser = new HashMap<>();
    /** 用户表 */
    List<UserDto> userDtoList = new ArrayList<>();

    public UserEndpoint() {
        /**
         * 假设五个用户，隶属于两个公司，我可能从公司ID查询，也根据条件查询全部
         */
        userDtoList.add(new UserDto(1L, "weidan", 6000L, 18));
        userDtoList.add(new UserDto(2L, "xiaodan", 10000L, 36));
        userDtoList.add(new UserDto(3L, "dadan", 9000L, 28));
        userDtoList.add(new UserDto(4L, "Sally", 12000L, 24));
        userDtoList.add(new UserDto(5L, "weisuodan", 20000L, 20));

        List<Long> userIds1 = new ArrayList<>(3);
        userIds1.add(1L);
        userIds1.add(3L);
        userIds1.add(4L);
        orgRelationUser.put(10L, userIds1);

        List<Long> userIds2 = new ArrayList<>(2);
        userIds2.add(2L);
        userIds2.add(5L);
        orgRelationUser.put(7L, userIds2);
    }

    // 接口的说明，可以指定接口做了什么事情
    @ApiOperation(value = "用户资源请求入口", notes = "可以根据公司的id查询到隶属于该公司的员工")
    @ApiImplicitParams({ // 参数集合，指定传递的参数的类型，是否必须，作用，数据类型等等
            @ApiImplicitParam(name = "orgId", value = "需要查询的公司的ID", required = true, paramType = "query", dataType = "String")
    })
    @GetMapping(params = "orgId")
    public List<UserDto> selectByOrg(@RequestParam("orgId") Long orgId) {
        List<Long> userIds = orgRelationUser.get(orgId);
        if (Objects.isNull(userIds) || userIds.isEmpty()) {
            return new ArrayList<>(0);
        }
        return userDtoList.stream().filter(userDto -> userIds.contains(userDto.getId())).collect(Collectors.toList());
    }

}
