package com.zerotrace.smartfacility.domain.repository;

import com.zerotrace.smartfacility.domain.model.Booking;
import com.zerotrace.smartfacility.domain.model.BookingStatus;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b from Booking b " +
        "where (:spaceId is null or b.space.id = :spaceId) " +
        "and (:status is null or b.status = :status) " +
        "and (:from is null or b.startAt >= :from) " +
        "and (:to is null or b.startAt <= :to)")
    Page<Booking> search(@Param("spaceId") Long spaceId,
                         @Param("status") BookingStatus status,
                         @Param("from") Instant from,
                         @Param("to") Instant to,
                         Pageable pageable);
}
