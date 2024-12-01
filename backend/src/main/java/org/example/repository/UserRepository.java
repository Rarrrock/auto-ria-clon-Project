package org.example.repository;

import org.example.entity.User;
import org.example.enums.AccountType;
import org.example.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // Поиск по email
    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findByRole(@Param("role") RoleEnum role);

    @Query("SELECT u FROM User u WHERE u.accountType = :accountType")
    List<User> findByAccountType(@Param("accountType") AccountType accountType);
}