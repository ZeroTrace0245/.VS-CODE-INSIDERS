package com.zerotrace.smartfacility.domain.repository;

import com.zerotrace.smartfacility.domain.model.Attachment;
import com.zerotrace.smartfacility.domain.model.MaintenanceTicket;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByTicket(MaintenanceTicket ticket);
}
