package cn.liweidan.springboot.security.service;

import cn.liweidan.springboot.security.dbo.User;
import cn.liweidan.springboot.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Description：
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/29 10:20 AM
 * @email toweidan@126.com
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService { // 实现 spring 提供的 UserDetailsService 类

    private UserRepository userRepository;

    /**
     * 使用一个 spring 提供的简单的密码编译器，用于密码的加密
     */
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserDetailServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * 实现 loadUserByUsername
     * 这个方法会根据用户名取出相对应的用户数据，然后相当于转换成 spring 定义的用户类
     * spring 会在拦截登陆，登录验证，权限验证等使用到这个用户对象
     * @param name 用户名
     * @return spring 定义的用户类
     * @throws UsernameNotFoundException 没有找到用户异常
     */
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByName(name);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException(name + " is not found");
        }
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(new SimpleGrantedAuthority(user.getRole()));

        return new org.springframework.security.core.userdetails.User(user.getName(), bCryptPasswordEncoder.encode("123"), grantedAuthorityList);
    }

}
