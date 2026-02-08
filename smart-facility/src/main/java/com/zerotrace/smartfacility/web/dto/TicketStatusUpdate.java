package com.zerotrace.smartfacility.web.dto;

import com.zerotrace.smartfacility.domain.model.TicketStatus;
import jakarta.validation.constraints.NotNull;

public record TicketStatusUpdate(@NotNull TicketStatus status) { }
