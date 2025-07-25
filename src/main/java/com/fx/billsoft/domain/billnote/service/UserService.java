// UserService.java
package com.fx.billsoft.domain.billnote.service;

import com.fx.billsoft.infrastructure.billnote.repository.UserRepository;
import com.fx.billsoft.domain.billnote.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(User user) {
        // 如果提供了明文密码，则进行加密
        if (user.getPlainPassword() != null && !user.getPlainPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPlainPassword()));
        }
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        // 如果提供了明文密码，则进行加密
        if (user.getPlainPassword() != null && !user.getPlainPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPlainPassword()));
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // 验证用户凭据
    public boolean validateUserCredentials(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            return false;
        }
        
        // 兼容明文密码和加密密码
        String storedPassword = user.get().getPassword();
        if (storedPassword.equals(password)) {
            // 明文密码匹配
            return true;
        }
        
        // 尝试使用BCrypt验证加密密码
        return passwordEncoder.matches(password, storedPassword);
    }
    
    // 根据用户名查找用户（用于登录）
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> {
                    String storedPassword = user.getPassword();
                    // 兼容明文密码和加密密码
                    if (storedPassword.equals(password)) {
                        // 明文密码匹配
                        return true;
                    }
                    // 尝试使用BCrypt验证加密密码
                    return passwordEncoder.matches(password, storedPassword);
                });
    }
    // 其他用户相关的业务逻辑方法
}