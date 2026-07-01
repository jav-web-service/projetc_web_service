package com.manage_and_book_badminton.dto;

import com.manage_and_book_badminton.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private boolean active;
}
