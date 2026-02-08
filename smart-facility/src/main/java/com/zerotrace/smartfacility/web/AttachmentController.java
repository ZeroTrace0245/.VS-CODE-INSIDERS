package com.zerotrace.smartfacility.web;

import com.zerotrace.smartfacility.domain.model.Attachment;
import com.zerotrace.smartfacility.service.AttachmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/tickets/{ticketId}/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MEMBER','MANAGER','ADMIN')")
    public Attachment upload(@PathVariable Long ticketId, @RequestPart("file") MultipartFile file) {
        return attachmentService.store(ticketId, file);
    }
}
