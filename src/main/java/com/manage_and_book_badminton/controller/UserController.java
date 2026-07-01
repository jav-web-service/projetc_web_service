package com.manage_and_book_badminton.controller;

import com.manage_and_book_badminton.dto.ResponseDTO;
import com.manage_and_book_badminton.dto.UserDTO;
import com.manage_and_book_badminton.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/admin/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Lấy danh sách, tìm kiếm, phân trang
    @GetMapping
    public ResponseEntity<ResponseDTO<Page<UserDTO>>> getAllUsers(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> users = userService.getAllUsers(search, pageable);
        
        return ResponseEntity.ok(new ResponseDTO<>(true, "Success", users, 200, LocalDateTime.now().toString()));
    }

    // Lấy chi tiết user
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserDTO>> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Success", user, 200, LocalDateTime.now().toString()));
    }

    // Cập nhật trạng thái (Block/Unblock)
    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseDTO<UserDTO>> updateUserStatus(
            @PathVariable Long id, 
            @RequestParam boolean active) {
        UserDTO updatedUser = userService.updateUserStatus(id, active);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Status updated successfully", updatedUser, 200, LocalDateTime.now().toString()));
    }

    // Xóa user (Soft delete được khuyến khích, nhưng theo FR-05 CRUD thì ta tạm thời làm Hard delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Object>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "User deleted successfully", null, 204, LocalDateTime.now().toString()));
    }
}
