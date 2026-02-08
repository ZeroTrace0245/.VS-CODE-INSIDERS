package com.zerotrace.smartfacility.web.dto;

import com.zerotrace.smartfacility.domain.model.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketRequest(
    @NotNull Long spaceId,
    @NotBlank String title,
    @NotBlank String description,
    @NotNull TicketPriority priority
) { }
