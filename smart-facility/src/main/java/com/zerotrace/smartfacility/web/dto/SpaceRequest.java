package com.zerotrace.smartfacility.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

public record SpaceRequest(
    @NotBlank String name,
    @NotBlank String location,
    @Min(1) Integer capacity,
    Set<String> features,
    boolean active
) { }
