package cn.liweidan.springboot.data.service;

import cn.liweidan.springboot.data.dbo.UserDo;
import cn.liweidan.springboot.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Description：用户业务层
 *
 * @author liweidan
 * @version 1.0
 * @date 2018-12-19 09:25
 * @email toweidan@126.com
 */
@Service
@Transactional
public class UserService {


    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void add(UserDo userDo) {
        userRepository.save(userDo);
    }

    public void update(UserDo userDo) {
        Optional<UserDo> origin = userRepository.findById(userDo.getId());
        origin.ifPresent(originUserDo -> originUserDo.setName(userDo.getName()));
    }

    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    public UserDo getById(Long userId) {
        return userRepository.findById(userId)
                .orElse(null);
    }


}
