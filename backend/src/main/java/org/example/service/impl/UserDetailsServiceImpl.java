package org.example.service.impl;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Ищу пользователя в базе данных
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с имейлом " + email + " не найден!"));

        // Возвращаю объект UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail()) // Использую email как username // Устанавливаю имя пользователя
                .password(user.getPassword()) // Устанавливаю зашифрованный пароль
                .roles(user.getRole().name()) // Устанавливаю роли пользователя
                .build();
    }
}