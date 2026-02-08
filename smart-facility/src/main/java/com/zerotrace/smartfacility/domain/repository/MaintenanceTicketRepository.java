package com.zerotrace.smartfacility.domain.repository;

import com.zerotrace.smartfacility.domain.model.MaintenanceTicket;
import com.zerotrace.smartfacility.domain.model.TicketPriority;
import com.zerotrace.smartfacility.domain.model.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MaintenanceTicketRepository extends JpaRepository<MaintenanceTicket, Long> {
    @Query("select t from MaintenanceTicket t " +
        "where (:status is null or t.status = :status) " +
        "and (:priority is null or t.priority = :priority) " +
        "and (:spaceId is null or t.space.id = :spaceId)")
    Page<MaintenanceTicket> search(@Param("status") TicketStatus status,
                                   @Param("priority") TicketPriority priority,
                                   @Param("spaceId") Long spaceId,
                                   Pageable pageable);
}
