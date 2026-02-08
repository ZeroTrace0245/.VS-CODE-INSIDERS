package com.zerotrace.smartfacility.web;

import com.zerotrace.smartfacility.domain.model.Role;
import com.zerotrace.smartfacility.domain.repository.UserRepository;
import com.zerotrace.smartfacility.service.auth.AuthService;
import com.zerotrace.smartfacility.web.dto.RegisterRequest;
import com.zerotrace.smartfacility.web.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        var saved = authService.register(request, Role.MEMBER);
        return new UserResponse(saved.getId(), saved.getEmail(), saved.getFullName(), saved.getRole(), saved.getCreatedAt());
    }

    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal UserDetails principal) {
        if (principal == null) {
            return null;
        }
        var user = userRepository.findByEmail(principal.getUsername()).orElse(null);
        if (user == null) {
            return null;
        }
        return new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getRole(), user.getCreatedAt());
    }
}
