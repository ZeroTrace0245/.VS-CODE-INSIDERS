package com.zerotrace.smartfacility.service;

import com.zerotrace.smartfacility.domain.model.Booking;
import com.zerotrace.smartfacility.domain.model.BookingStatus;
import com.zerotrace.smartfacility.domain.model.Space;
import com.zerotrace.smartfacility.domain.model.User;
import com.zerotrace.smartfacility.domain.repository.BookingRepository;
import com.zerotrace.smartfacility.domain.repository.SpaceRepository;
import com.zerotrace.smartfacility.domain.repository.UserRepository;
import com.zerotrace.smartfacility.exception.BadRequestException;
import com.zerotrace.smartfacility.exception.NotFoundException;
import com.zerotrace.smartfacility.web.dto.BookingRequest;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SpaceRepository spaceRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, SpaceRepository spaceRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.spaceRepository = spaceRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Booking create(Long userId, BookingRequest request) {
        if (request.startAt().isAfter(request.endAt()) || request.startAt().equals(request.endAt())) {
            throw new BadRequestException("Start time must be before end time");
        }
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found"));
        Space space = spaceRepository.findById(request.spaceId())
            .orElseThrow(() -> new NotFoundException("Space not found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setSpace(space);
        booking.setStartAt(request.startAt());
        booking.setEndAt(request.endAt());
        booking.setStatus(BookingStatus.PENDING);
        booking.setPurpose(request.purpose());
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking updateStatus(Long bookingId, BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new NotFoundException("Booking not found"));
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public Page<Booking> search(Long spaceId, BookingStatus status, Instant from, Instant to, Pageable pageable) {
        return bookingRepository.search(spaceId, status, from, to, pageable);
    }
}
