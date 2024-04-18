package com.api.barber.repositories;

import com.api.barber.domain.entities.Appointment;
import com.api.barber.domain.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    @Query(value = """
            SELECT ap.*
            FROM appointments ap
            WHERE ap.barber_id = :barberId
            AND (:status IS NULL OR ap.status = UPPER(:status))
            AND ap.date >= COALESCE(:startDate, ap.date) AND ap.date <= COALESCE(:endDate, ap.date)
            """, nativeQuery = true)
    Page<Appointment> findByBarberIdAndStatusAndDateBetween(
            @Param("barberId") UUID barberId,
            @Param("status") String status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    @Query(value = """
            SELECT ap.*
            FROM appointments ap
            WHERE ap.customer_id = :customerId
            AND (:status IS NULL OR ap.status = UPPER(:status))
            AND ap.date >= COALESCE(:startDate, ap.date) AND ap.date <= COALESCE(:endDate, ap.date)
            """, nativeQuery = true)
    Page<Appointment> findByCustomerIdAndStatusAndDateBetween(
            @Param("customerId") UUID customerId,
            @Param("status") String status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    @Query(value = """
            SELECT ap.*
            FROM appointments ap
            WHERE (:status IS NULL OR ap.status = UPPER(:status))
            AND ap.date >= COALESCE(:startDate, ap.date) AND ap.date <= COALESCE(:endDate, ap.date)
            """, nativeQuery = true)
    Page<Appointment> findAllByStatusAndDateBetween(String status,
                                                    LocalDate startDate,
                                                    LocalDate endDate,
                                                    Pageable pageable);

    boolean existsByBarberIdAndCustomerIdAndWorkingHourIdAndBarberServiceIdAndDate(UUID barberId, UUID customerId, UUID workingHourId, UUID barberServiceId, LocalDate date);

}
