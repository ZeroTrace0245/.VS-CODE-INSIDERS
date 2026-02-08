package com.zerotrace.smartfacility.web.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record BookingRequest(
    @NotNull Long spaceId,
    @Future @NotNull Instant startAt,
    @Future @NotNull Instant endAt,
    @NotBlank String purpose
) { }
