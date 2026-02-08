package com.zerotrace.smartfacility.web;

import com.zerotrace.smartfacility.domain.model.Booking;
import com.zerotrace.smartfacility.domain.model.BookingStatus;
import com.zerotrace.smartfacility.domain.repository.UserRepository;
import com.zerotrace.smartfacility.exception.NotFoundException;
import com.zerotrace.smartfacility.service.BookingService;
import com.zerotrace.smartfacility.web.dto.BookingRequest;
import com.zerotrace.smartfacility.web.dto.BookingStatusUpdate;
import jakarta.validation.Valid;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserRepository userRepository;

    public BookingController(BookingService bookingService, UserRepository userRepository) {
        this.bookingService = bookingService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public Page<Booking> list(@PageableDefault(size = 20, sort = "startAt") Pageable pageable,
                             @RequestParam(required = false) Long spaceId,
                             @RequestParam(required = false) BookingStatus status,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        return bookingService.search(spaceId, status, from, to, pageable);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MEMBER','MANAGER','ADMIN')")
    public Booking create(@AuthenticationPrincipal UserDetails principal,
                          @Valid @RequestBody BookingRequest request) {
        Long userId = resolveUserId(principal);
        return bookingService.create(userId, request);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public Booking updateStatus(@PathVariable Long id, @Valid @RequestBody BookingStatusUpdate update) {
        BookingStatus status = update.status();
        return bookingService.updateStatus(id, status);
    }

    private Long resolveUserId(UserDetails principal) {
        return userRepository.findByEmail(principal.getUsername())
            .map(u -> u.getId())
            .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
