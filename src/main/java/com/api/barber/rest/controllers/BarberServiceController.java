package com.api.barber.rest.controllers;

import com.api.barber.rest.dtos.request.BarberServiceStatusRequestDto;
import com.api.barber.rest.dtos.response.BarberServiceResponseDto;
import com.api.barber.services.BarberServiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/services")
public class BarberServiceController {

    private final BarberServiceService barberServiceService;
    @GetMapping("/all")
    public ResponseEntity<Page<BarberServiceResponseDto>> findAll(Pageable pageable) {
        try {
            Page<BarberServiceResponseDto> services = barberServiceService.findAll(pageable);
            log.info("Found all services successfully");
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            log.error("Error listing services: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/active")
    public ResponseEntity<Page<BarberServiceResponseDto>> findAllActiveStatus(Pageable pageable) {
        try {
            Page<BarberServiceResponseDto> services = barberServiceService.findAllActiveStatus(pageable);
            log.info("Found all active services successfully");
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            log.error("Error listing services: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<BarberServiceResponseDto> findBarberServiceById(@PathVariable UUID id) {
        try {
            BarberServiceResponseDto service = barberServiceService.findById(id);
            log.info("Found service successfully with id: {}", id);
            return ResponseEntity.ok(service);
        } catch (Exception e) {
            log.error("Error listing service with id: {}. Error message: {}", id, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @PostMapping("")
    public ResponseEntity<BarberServiceResponseDto> saveService(@RequestParam(value = "title") String title,
                                         @RequestParam(value = "description", required = false) String description,
                                         @RequestParam(value = "price") Double price,
                                         @RequestParam(value = "image",required = false) MultipartFile imageFile) {
        try {
            BarberServiceResponseDto response = this.barberServiceService.saveService(title, description, price, imageFile);
            log.info("Service registered successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error registering service {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateService(@RequestParam(value = "title") String title,
                                           @RequestParam(value = "description",required = false) String description,
                                           @RequestParam(value = "price") Double price,
                                           @RequestParam(value = "image",required = false) MultipartFile imageFile,
                                           @PathVariable("id") UUID id) {
        try {
            this.barberServiceService.updateService(title, description, price, imageFile, id);
            log.info("Service updated successfully with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error updating the service: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<?> updateServiceStatus(@PathVariable("id") UUID id,
                                                 @RequestBody BarberServiceStatusRequestDto request) {
        try {
            this.barberServiceService.updateServiceStatus(id, request);
            log.info("Service status updated successfully with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error updating service status: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
