package com.zerotrace.smartfacility.web.dto;

import com.zerotrace.smartfacility.domain.model.BookingStatus;
import jakarta.validation.constraints.NotNull;

public record BookingStatusUpdate(@NotNull BookingStatus status) { }
