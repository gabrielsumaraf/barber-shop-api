package com.api.barber.rest.controllers;

import com.api.barber.domain.enums.AppointmentStatus;
import com.api.barber.rest.dtos.request.AppointmentRequestDto;
import com.api.barber.rest.dtos.request.AppointmentStatusUpdateRequestDto;
import com.api.barber.rest.dtos.request.CustomerAppointmentResponseDto;
import com.api.barber.rest.dtos.response.BarberAppointmentResponseDto;
import com.api.barber.rest.dtos.response.OwnerAppointmentResponseDto;
import com.api.barber.rest.dtos.response.WorkingHourResponseDto;
import com.api.barber.services.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/barbers/history")
    public ResponseEntity<Page<BarberAppointmentResponseDto>> findAppointmentsByBarberId(Principal principal,
                                                                                        @RequestParam(value = "status",required = false) AppointmentStatus status,
                                                                                        @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                                                                        @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                                                                        Pageable pageable) {
        try {
            Page<BarberAppointmentResponseDto> response = this.appointmentService.findAppointmentsByBarberId(
                    principal,
                    status,
                    startDate,
                    endDate,
                    pageable);
            log.info("Appointments by barber fetched successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/customers/history")
    public Page<CustomerAppointmentResponseDto> findAppointmentsByCustomerId(Principal principal,
                                                                            @RequestParam(value = "status", required = false) AppointmentStatus status,
                                                                            @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                                                            @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                                                            Pageable pageable) {
        try {
            Page<CustomerAppointmentResponseDto> response = this.appointmentService.findAppointmentsByCustomerId(
                    principal,
                    status,
                    startDate,
                    endDate,
                    pageable);
            log.info("Appointments by customer fetched successfully.");
            return response;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/history/all")
    public Page<OwnerAppointmentResponseDto> findAllAppointments(@RequestParam(value = "status",required = false) AppointmentStatus status,
                                                                @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                                                @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                                                Pageable pageable) {
        try {
            Page<OwnerAppointmentResponseDto> response = this.appointmentService.findAllAppointments(
                    status,
                    startDate,
                    endDate,
                    pageable);
            log.info("All appointments fetched successfully.");
            return response;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> saveAppointment(Principal principal, @RequestBody AppointmentRequestDto request) {
        try {
            this.appointmentService.saveAppointment(principal, request);
            log.info("Appointment saved successfully.");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/working-hours")
    public ResponseEntity<List<WorkingHourResponseDto>> findAvailableHours(@RequestParam("date") LocalDate localDate,
                                                                           @RequestParam("barberId") UUID barberId) {
        try {
            List<WorkingHourResponseDto> response = this.appointmentService.findAvailableHours(localDate, barberId);
            log.info("Available working hours found successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointmentStatus(Principal principal, @PathVariable("id") UUID id,
                                                     @RequestBody AppointmentStatusUpdateRequestDto request) {
        try {
            this.appointmentService.updateAppointmentStatus(principal, id, request);
            log.info("Appointment status updated successfully.");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
