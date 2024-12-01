package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserRequest;
import org.example.dto.UserResponse;
import org.example.enums.AccountType;
import org.example.enums.RoleEnum;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    // Возвращаю всех пользователей в формате UserResponse
    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Ищу пользователя по ID
    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID " + id + " не найден."));
        return mapToResponse(user);
    }

    // Создаю и сохраняю нового пользователя
    @Override
    public UserResponse createUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setRole(userRequest.getRole());
        user.setAccountType(userRequest.getAccountType() != null ? userRequest.getAccountType() : AccountType.BASIC); // Установка BASIC по умолчанию
        return mapToResponse(userRepository.save(user));
    }

    // Обновляю данные пользователя
    @Override
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID " + id + " не найден."));

        existingUser.setUsername(userRequest.getUsername());
        existingUser.setEmail(userRequest.getEmail());
        existingUser.setPassword(userRequest.getPassword());
        existingUser.setRole(userRequest.getRole());
        return mapToResponse(userRepository.save(existingUser));
    }

    @Override
    public void updateUserRole(Long userId, RoleEnum newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + userId + " не найден."));

        if (user.getRole() == RoleEnum.ADMIN || user.getRole() == RoleEnum.MANAGER) {
            throw new SecurityException("Невозможно изменить роль ADMIN или MANAGER.");
        }

        user.setRole(newRole);
        userRepository.save(user);
    }

    // Удаляю пользователя по ID
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Ищу пользователя по email
    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::mapToResponse);
    }

    // Преобразую User в UserResponse
    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    // ППроверяю роль
    @Override
    public boolean isAdminOrManager(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        return user.getRole() == RoleEnum.ADMIN || user.getRole() == RoleEnum.MANAGER;
    }
}