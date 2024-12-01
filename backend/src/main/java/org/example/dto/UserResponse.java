package org.example.dto;

import lombok.Builder;
import lombok.Data;
import org.example.enums.RoleEnum;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String password;
    private RoleEnum role;
}
