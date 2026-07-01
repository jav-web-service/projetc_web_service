package com.manage_and_book_badminton.service;

import com.manage_and_book_badminton.dto.UserDTO;
import com.manage_and_book_badminton.entity.User;
import com.manage_and_book_badminton.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Page<UserDTO> getAllUsers(String search, Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        
        // Convert to Stream, apply search filter if any, and map to DTO (as required by UC-02)
        List<UserDTO> filteredUsers = usersPage.stream()
                .filter(user -> search == null || search.isEmpty() || 
                        user.getUsername().toLowerCase().contains(search.toLowerCase()) || 
                        user.getEmail().toLowerCase().contains(search.toLowerCase()))
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(filteredUsers, pageable, usersPage.getTotalElements());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDTO(user);
    }

    public UserDTO updateUserStatus(Long id, boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(active);
        return mapToDTO(userRepository.save(user));
    }
    
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    private UserDTO mapToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .build();
    }
}
