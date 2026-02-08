package com.zerotrace.smartfacility.service;

import com.zerotrace.smartfacility.domain.model.Attachment;
import com.zerotrace.smartfacility.domain.model.MaintenanceTicket;
import com.zerotrace.smartfacility.domain.repository.AttachmentRepository;
import com.zerotrace.smartfacility.domain.repository.MaintenanceTicketRepository;
import com.zerotrace.smartfacility.exception.BadRequestException;
import com.zerotrace.smartfacility.exception.NotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AttachmentService {

    private static final Logger log = LoggerFactory.getLogger(AttachmentService.class);

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    private final AttachmentRepository attachmentRepository;
    private final MaintenanceTicketRepository ticketRepository;

    public AttachmentService(AttachmentRepository attachmentRepository, MaintenanceTicketRepository ticketRepository) {
        this.attachmentRepository = attachmentRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public Attachment store(Long ticketId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }
        if (file.getSize() > 5 * 1024 * 1024) { // 5MB guard
            throw new BadRequestException("File too large (max 5MB)");
        }
        MaintenanceTicket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new NotFoundException("Ticket not found"));

        try {
            Path uploadPath = Path.of(uploadDir).toAbsolutePath();
            Files.createDirectories(uploadPath);
            String safeName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            Path target = uploadPath.resolve(safeName).normalize();
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            Attachment attachment = new Attachment();
            attachment.setTicket(ticket);
            attachment.setFilename(file.getOriginalFilename());
            attachment.setStoragePath(target.toString());
            attachment.setContentType(file.getContentType());
            attachment.setSizeBytes(file.getSize());
            return attachmentRepository.save(attachment);
        } catch (IOException e) {
            log.error("Failed to store attachment", e);
            throw new BadRequestException("Unable to store file");
        }
    }
}
