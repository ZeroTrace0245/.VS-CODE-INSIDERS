package com.zerotrace.smartfacility.domain.repository;

import com.zerotrace.smartfacility.domain.model.Space;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceRepository extends JpaRepository<Space, Long> {
    List<Space> findByActiveTrue();
}
