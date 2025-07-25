package com.fx.billsoft.interfaces;

import com.fx.billsoft.domain.billnote.entity.AuthRequest;
import com.fx.billsoft.domain.billnote.entity.AuthResponse;
import com.fx.billsoft.domain.billnote.entity.User;
import com.fx.billsoft.domain.billnote.service.UserService;
import com.fx.billsoft.infrastructure.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    @Autowired
    public AuthController(UserService userService, JWTUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            // 检查用户名是否已存在
            if (userService.findByUsername(user.getUsername()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body("用户名已存在");
            }

            // 检查邮箱是否已存在
            if (user.getEmail() != null && userService.findByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body("邮箱已被注册");
            }

            // 设置创建时间
            user.setCreateTime(LocalDateTime.now());
            user.setCreateUser(user.getUsername());
            user.setUpdateTime(LocalDateTime.now());
            
            // 确保不会返回密码
            user.setPlainPassword(user.getPassword());

            // 保存用户（密码会在服务层自动加密）
            User savedUser = userService.saveUser(user);
            
            // 清除密码字段，避免返回给前端
            savedUser.setPassword(null);

            // 生成JWT令牌
            final String token = jwtUtil.generateToken(user.getUsername());

            // 返回认证响应
            return ResponseEntity.ok(new AuthResponse(token, savedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("注册失败: " + e.getMessage());
        }
    }

    /**
     * 用户登录
     * @param authRequest 登录请求
     * @return 登录结果
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthRequest authRequest) {
        try {
            // 验证用户凭据
            Optional<User> user = userService.findByUsernameAndPassword(
                    authRequest.getUsername(), 
                    authRequest.getPassword());

            if (user.isPresent()) {
                // 生成JWT令牌
                final String token = jwtUtil.generateToken(authRequest.getUsername());
                
                // 清除密码字段，避免返回给前端
                User userWithoutPassword = user.get();
                userWithoutPassword.setPassword(null);
                
                // 返回认证响应
                return ResponseEntity.ok(new AuthResponse(token, userWithoutPassword));
            } else {
                return ResponseEntity.badRequest()
                        .body("用户名或密码错误");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("登录失败: " + e.getMessage());
        }
    }
}