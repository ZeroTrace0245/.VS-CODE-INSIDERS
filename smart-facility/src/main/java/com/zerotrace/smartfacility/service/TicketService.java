package com.zerotrace.smartfacility.service;

import com.zerotrace.smartfacility.domain.model.MaintenanceTicket;
import com.zerotrace.smartfacility.domain.model.Space;
import com.zerotrace.smartfacility.domain.model.TicketPriority;
import com.zerotrace.smartfacility.domain.model.TicketStatus;
import com.zerotrace.smartfacility.domain.model.User;
import com.zerotrace.smartfacility.domain.repository.MaintenanceTicketRepository;
import com.zerotrace.smartfacility.domain.repository.SpaceRepository;
import com.zerotrace.smartfacility.domain.repository.UserRepository;
import com.zerotrace.smartfacility.exception.NotFoundException;
import com.zerotrace.smartfacility.web.dto.TicketRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {

    private final MaintenanceTicketRepository ticketRepository;
    private final SpaceRepository spaceRepository;
    private final UserRepository userRepository;

    public TicketService(MaintenanceTicketRepository ticketRepository, SpaceRepository spaceRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.spaceRepository = spaceRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public MaintenanceTicket create(Long userId, TicketRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found"));
        Space space = spaceRepository.findById(request.spaceId())
            .orElseThrow(() -> new NotFoundException("Space not found"));

        MaintenanceTicket ticket = new MaintenanceTicket();
        ticket.setSpace(space);
        ticket.setReporter(user);
        ticket.setTitle(request.title());
        ticket.setDescription(request.description());
        ticket.setPriority(request.priority());
        ticket.setStatus(TicketStatus.OPEN);
        return ticketRepository.save(ticket);
    }

    @Transactional
    public MaintenanceTicket updateStatus(Long id, TicketStatus status) {
        MaintenanceTicket ticket = ticketRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Ticket not found"));
        ticket.setStatus(status);
        return ticketRepository.save(ticket);
    }

    public Page<MaintenanceTicket> search(TicketStatus status, TicketPriority priority, Long spaceId, Pageable pageable) {
        return ticketRepository.search(status, priority, spaceId, pageable);
    }
}
