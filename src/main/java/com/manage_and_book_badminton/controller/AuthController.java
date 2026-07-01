package com.manage_and_book_badminton.controller;

import com.manage_and_book_badminton.dto.*;
import com.manage_and_book_badminton.entity.Role;
import com.manage_and_book_badminton.entity.TokenBlacklist;
import com.manage_and_book_badminton.entity.User;
import com.manage_and_book_badminton.repository.TokenBlacklistRepository;
import com.manage_and_book_badminton.repository.UserRepository;
import com.manage_and_book_badminton.security.JwtUtils;
import com.manage_and_book_badminton.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    TokenBlacklistRepository tokenBlacklistRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<JwtResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        JwtResponse jwtResponse = new JwtResponse(jwt, refreshToken, userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(), roles);

        return ResponseEntity.ok(new ResponseDTO<>(true, "Login successful", jwtResponse, 200, LocalDateTime.now().toString()));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<Object>> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Conflict: Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Conflict: Error: Email is already in use!");
        }

        // Create new user's account with default ROLE_CUSTOMER
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .role(Role.ROLE_CUSTOMER)
                .active(true)
                .build();

        userRepository.save(user);

        return new ResponseEntity<>(new ResponseDTO<>(true, "User registered successfully!", null, 201, LocalDateTime.now().toString()), HttpStatus.CREATED);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ResponseDTO<Object>> logoutUser(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            String jwt = headerAuth.substring(7);
            
            // Get expiration date
            Date expiryDate = jwtUtils.getExpirationDateFromToken(jwt);
            LocalDateTime localDateTime = expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            
            TokenBlacklist tokenBlacklist = TokenBlacklist.builder()
                    .token(jwt)
                    .expiryDate(localDateTime)
                    .build();
            tokenBlacklistRepository.save(tokenBlacklist);
            
            return ResponseEntity.ok(new ResponseDTO<>(true, "Logged out successfully", null, 200, LocalDateTime.now().toString()));
        }
        
        return ResponseEntity.badRequest().body(new ResponseDTO<>(false, "No token found", null, 400, LocalDateTime.now().toString()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseDTO<TokenRefreshResponse>> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        
        if (jwtUtils.validateJwtToken(requestRefreshToken)) {
            String username = jwtUtils.getUserNameFromJwtToken(requestRefreshToken);
            org.springframework.security.core.userdetails.UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(username).password("").authorities("ROLE_CUSTOMER").build();
            Authentication authentication = new UsernamePasswordAuthenticationToken(UserDetailsImpl.build(userRepository.findByUsername(username).get()), null, null);
            
            String token = jwtUtils.generateJwtToken(authentication);
            return ResponseEntity.ok(new ResponseDTO<>(true, "Token refreshed successfully", new TokenRefreshResponse(token, requestRefreshToken), 200, LocalDateTime.now().toString()));
        }
        
        throw new RuntimeException("Refresh token is invalid!");
    }

    @PostMapping("/change-password")
    public ResponseEntity<ResponseDTO<Object>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body(new ResponseDTO<>(false, "Unauthorized", null, 401, LocalDateTime.now().toString()));
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(new ResponseDTO<>(false, "Old password does not match", null, 400, LocalDateTime.now().toString()));
        }

        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(new ResponseDTO<>(true, "Password changed successfully", null, 200, LocalDateTime.now().toString()));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseDTO<Object>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Error: Email not found!"));

        // Generate a new random password for simplicity (In real projects, we send an OTP/Link)
        String newRandomPassword = "Pass" + (int)(Math.random() * 1000000);
        user.setPassword(encoder.encode(newRandomPassword));
        userRepository.save(user);

        // Giả lập việc gửi Email bằng cách in ra màn hình Console
        System.out.println("=========================================================");
        System.out.println("EMAIL SENT TO: " + user.getEmail());
        System.out.println("YOUR NEW PASSWORD IS: " + newRandomPassword);
        System.out.println("=========================================================");

        return ResponseEntity.ok(new ResponseDTO<>(true, "A new password has been sent to your email (Check console log)", null, 200, LocalDateTime.now().toString()));
    }
}
