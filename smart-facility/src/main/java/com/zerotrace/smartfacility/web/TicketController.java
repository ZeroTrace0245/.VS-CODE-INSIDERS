package com.zerotrace.smartfacility.web;

import com.zerotrace.smartfacility.domain.model.MaintenanceTicket;
import com.zerotrace.smartfacility.domain.model.TicketPriority;
import com.zerotrace.smartfacility.domain.model.TicketStatus;
import com.zerotrace.smartfacility.domain.repository.UserRepository;
import com.zerotrace.smartfacility.exception.NotFoundException;
import com.zerotrace.smartfacility.service.TicketService;
import com.zerotrace.smartfacility.web.dto.TicketRequest;
import com.zerotrace.smartfacility.web.dto.TicketStatusUpdate;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final UserRepository userRepository;

    public TicketController(TicketService ticketService, UserRepository userRepository) {
        this.ticketService = ticketService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public Page<MaintenanceTicket> list(@PageableDefault(size = 20, sort = "createdAt") Pageable pageable,
                                        @RequestParam(required = false) TicketStatus status,
                                        @RequestParam(required = false) TicketPriority priority,
                                        @RequestParam(required = false) Long spaceId) {
        return ticketService.search(status, priority, spaceId, pageable);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MEMBER','MANAGER','ADMIN')")
    public MaintenanceTicket create(@AuthenticationPrincipal UserDetails principal,
                                    @Valid @RequestBody TicketRequest request) {
        Long userId = resolveUserId(principal);
        return ticketService.create(userId, request);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public MaintenanceTicket updateStatus(@PathVariable Long id, @Valid @RequestBody TicketStatusUpdate update) {
        return ticketService.updateStatus(id, update.status());
    }

    private Long resolveUserId(UserDetails principal) {
        return userRepository.findByEmail(principal.getUsername())
            .map(u -> u.getId())
            .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
