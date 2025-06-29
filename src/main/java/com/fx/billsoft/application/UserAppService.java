// UserAppService.java
package com.fx.billsoft.application;

import com.fx.billsoft.domain.billnote.entity.User;
import com.fx.billsoft.domain.billnote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserAppService {

    private final UserService userService;

    @Autowired
    public UserAppService(UserService userService) {
        this.userService = userService;
    }

    public User createUser(User user) {
        return userService.saveUser(user);
    }

    public Optional<User> getUserById(Long userId) {
        return userService.getUserById(userId);
    }

    public User updateUser(User user) {
        return userService.updateUser(user);
    }

    public void deleteUser(Long userId) {
        userService.deleteUser(userId);
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
