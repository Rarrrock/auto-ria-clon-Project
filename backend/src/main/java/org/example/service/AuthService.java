package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.dto.UserRequest;
import org.example.dto.UserResponse;
import org.example.enums.AccountType;
import org.example.enums.RoleEnum;
import org.example.entity.User;
import org.example.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void register(RegisterRequest request) {
        // Проверяю наличие пользователя
        if (userService.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email уже используется!");
        }

        // Создаю DTO для передачи данных
        UserRequest userRequest = UserRequest.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(RoleEnum.USER) // По умолчанию роль USER
                .accountType(AccountType.BASIC) // По умолчанию BASIC
                .build();

        userService.createUser(userRequest);
    }

    public String login(LoginRequest request) {
        // Ищу пользователя
        UserResponse userResponse = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким email не найден!"));

        // Проверяю пароль
        if (!passwordEncoder.matches(request.getPassword(), userResponse.getPassword())) {
            throw new IllegalArgumentException("Неверный email или пароль!");
        }

        // Генерирую токен
        return jwtUtil.generateToken(userResponse.getEmail(), userResponse.getRole().name());
    }

    public UserResponse getCurrentUser(String email) {
        return userService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден!"));
    }

    // Преобразую UserResponse в User (для работы с базой)
    private User mapToUser(UserResponse userResponse) {
        User user = new User();
        user.setId(userResponse.getId());
        user.setUsername(userResponse.getUsername());
        user.setEmail(userResponse.getEmail());
        user.setRole(userResponse.getRole());
        return user;
    }
}