package com.api.barber.repositories;

import com.api.barber.domain.entities.BarberService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BarberServiceRepository extends JpaRepository<BarberService, UUID> {
}
