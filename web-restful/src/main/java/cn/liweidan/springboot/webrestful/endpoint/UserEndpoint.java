package cn.liweidan.springboot.webrestful.endpoint;

import cn.liweidan.springboot.webrestful.dbo.UserDo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    List<UserDo> userDoList = new ArrayList<>();

    public UserEndpoint() {
        /**
         * 假设五个用户，隶属于两个公司，我可能从公司ID查询，也根据条件查询全部
         */
        userDoList.add(new UserDo(1L, "weidan", 6000L, 18));
        userDoList.add(new UserDo(2L, "xiaodan", 10000L, 36));
        userDoList.add(new UserDo(3L, "dadan", 9000L, 28));
        userDoList.add(new UserDo(4L, "Sally", 12000L, 24));
        userDoList.add(new UserDo(5L, "weisuodan", 20000L, 20));

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

    @GetMapping(params = "orgId")
    public List<UserDo> selectByOrg(@RequestParam("orgId") Long orgId) {
        List<Long> userIds = orgRelationUser.get(orgId);
        if (Objects.isNull(userIds) || userIds.isEmpty()) {
            return new ArrayList<>(0);
        }
        return userDoList.stream().filter(userDo -> userIds.contains(userDo.getId())).collect(Collectors.toList());
    }

    @GetMapping(params = {"salaryMin", "salaryMax"})
    public List<UserDo> selectAllByCondition(@RequestParam(value = "salaryMin", defaultValue = "0") Long salaryMin,
                                             @RequestParam(value = "salaryMax", defaultValue = Long.MAX_VALUE + "") Long salaryMax) {
        return userDoList.stream()
                .filter(userDo -> userDo.getSalary() > salaryMin && userDo.getSalary() < salaryMax)
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<UserDo> selectAll() {
        return userDoList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@RequestBody UserDo userDo, @RequestParam("orgId") Long orgId) {
        Long nextId = (long) (userDoList.size() + 1);
        userDo.setId(nextId);
        userDoList.add(userDo);

        List<Long> userIds = orgRelationUser.get(orgId);
        if (Objects.isNull(userIds)) {
            userIds = new ArrayList<>();
        }
        userIds.add(nextId);
        orgRelationUser.put(orgId, userIds);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@RequestBody UserDo userDo) {
        for (UserDo origin : userDoList) {
            if (origin.getId().equals(userDo.getId())) {
                origin.setName(userDo.getName());
                origin.setSalary(userDo.getSalary());
                origin.setAge(userDo.getAge());
                break;
            }
        }
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        userDoList = userDoList.stream().filter(userDo -> !userDo.getId().equals(userId)).collect(Collectors.toList());
    }

}
