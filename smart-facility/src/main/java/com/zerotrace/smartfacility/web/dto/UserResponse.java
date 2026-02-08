package com.zerotrace.smartfacility.web.dto;

import com.zerotrace.smartfacility.domain.model.Role;
import java.time.Instant;

public record UserResponse(
    Long id,
    String email,
    String fullName,
    Role role,
    Instant createdAt
) { }
