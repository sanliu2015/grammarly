package com.plq.grammarly.service.impl;

import com.plq.grammarly.exception.CustomException;
import com.plq.grammarly.model.AuthenticationRequest;
import com.plq.grammarly.model.MyUserDetails;
import com.plq.grammarly.model.entity.User;
import com.plq.grammarly.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * This is Description
 *
 * @author luquan.peng
 * @date 2021/06/16
 */
@Service
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
        return user.map(MyUserDetails::new).get();
    }

    /**
     * 初始化用户
     */
    @PostConstruct
    public void initUserinfo() {
        Optional<User> user = userRepository.findByUsername("admin");
        if (!user.isPresent()) {
            User newUser = User.builder()
                    .username("admin")
                    .password("$2a$10$SH8NR.SXX6fGJiDxa2RsLO326bAVxaihQppa8HH1fipREHaobLn2W")
                    .roles("ROLE_ADMIN")
                    .active(true).build();
            userRepository.insert(newUser);
        }
    }

    public void modifyPwd(AuthenticationRequest authenticationRequest) {
        Optional<User> user = userRepository.findByUsername("admin");
        if (user.isPresent()) {
            User obj = user.get();
            BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
            String newPassword = bcryptPasswordEncoder.encode(authenticationRequest.getPassword());
            obj.setPassword(newPassword);
            userRepository.save(obj);
        } else {
            throw new CustomException("用户名不存在");
        }
    }
}
