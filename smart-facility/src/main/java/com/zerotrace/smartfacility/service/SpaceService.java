package com.zerotrace.smartfacility.service;

import com.zerotrace.smartfacility.domain.model.Space;
import com.zerotrace.smartfacility.domain.repository.SpaceRepository;
import com.zerotrace.smartfacility.exception.NotFoundException;
import com.zerotrace.smartfacility.web.dto.SpaceRequest;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpaceService {

    private final SpaceRepository spaceRepository;

    public SpaceService(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    @Cacheable("spaces-active")
    public List<Space> listActive() {
        return spaceRepository.findByActiveTrue();
    }

    public List<Space> listAll() {
        return spaceRepository.findAll();
    }

    @Transactional
    @CacheEvict(value = "spaces-active", allEntries = true)
    public Space create(SpaceRequest request) {
        Space space = new Space();
        space.setName(request.name());
        space.setLocation(request.location());
        space.setCapacity(request.capacity());
        space.setFeatures(request.features());
        space.setActive(request.active());
        return spaceRepository.save(space);
    }

    @Transactional
    @CacheEvict(value = "spaces-active", allEntries = true)
    public Space update(Long id, SpaceRequest request) {
        Space space = get(id);
        space.setName(request.name());
        space.setLocation(request.location());
        space.setCapacity(request.capacity());
        space.setFeatures(request.features());
        space.setActive(request.active());
        return spaceRepository.save(space);
    }

    @Transactional(readOnly = true)
    public Space get(Long id) {
        return spaceRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Space not found"));
    }

    @Transactional
    @CacheEvict(value = "spaces-active", allEntries = true)
    public void delete(Long id) {
        spaceRepository.deleteById(id);
    }
}
