package com.api.barber.rest.controllers;

import com.api.barber.rest.dtos.request.BarberEmployeeRequestDto;
import com.api.barber.rest.dtos.request.BarberEmployeeStatusRequestDto;
import com.api.barber.rest.dtos.response.BarberEmployeeResponseDto;
import com.api.barber.services.BarberEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/barbers")
public class BarberEmployeeController {

    private final BarberEmployeeService barberEmployeeService;

    @GetMapping("/all")
    public ResponseEntity<Page<BarberEmployeeResponseDto>> findAllBarbers(Pageable pageable) {
        try {
            Page<BarberEmployeeResponseDto> response = this.barberEmployeeService.findAllBarbers(pageable);
            log.info("Found all barbers successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error listing all barbers {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<Page<BarberEmployeeResponseDto>> findAllActiveBarbers(Pageable pageable) {
        try {
            Page<BarberEmployeeResponseDto> response = this.barberEmployeeService.findAllActiveBarbers(pageable);
            log.info("Found all active barbers successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error listing active barbers {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarberEmployeeResponseDto> findBarberById(@PathVariable UUID id, Principal principal) {
        try {
            BarberEmployeeResponseDto response = this.barberEmployeeService.findBarberById(id, principal);
            log.info("Found barber successfully with id: {}.", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error finding barber with id: {}. Error message: {}", id, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @PostMapping("")
    public ResponseEntity<BarberEmployeeResponseDto> saveBarber(@RequestBody BarberEmployeeRequestDto request) {
        try {
            BarberEmployeeResponseDto response = this.barberEmployeeService.saveBarber(request);
            log.info("Barber registered successfully: {}", request.getPhone());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error registering barber: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}/toggle-status")
    public void updateBarberStatus(@PathVariable("id") UUID barberId, @RequestBody BarberEmployeeStatusRequestDto request) throws Exception {
        this.barberEmployeeService.updateBarberStatus(barberId, request);
    }
}

