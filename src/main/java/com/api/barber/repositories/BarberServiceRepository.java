package com.api.barber.repositories;

import com.api.barber.domain.entities.BarberService;
import com.api.barber.domain.enums.BarberServiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BarberServiceRepository extends JpaRepository<BarberService, UUID> {
    Page<BarberService> findAllByStatus(BarberServiceStatus barberServiceStatus, Pageable pageable);
}
