package com.api.barber.rest.controllers;

import com.api.barber.rest.dtos.response.BarberServiceResponseDto;
import com.api.barber.services.BarberServiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/services")
public class BarberServiceController {

    private final BarberServiceService barberServiceService;
    @GetMapping("")
    public ResponseEntity<List<BarberServiceResponseDto>> findAllServices() {
        try {
            List<BarberServiceResponseDto> services = this.barberServiceService.findAllServices();
            log.info("Found all services successfully");
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            log.error("Error listing services: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
    @PostMapping("")
    public ResponseEntity<?> saveService(@RequestParam("title") String title,
                                         @RequestParam("description") String description,
                                         @RequestParam("price") Double price,
                                         @RequestParam("image") MultipartFile imageFile) {
        try {
            this.barberServiceService.saveService(title,description,price,imageFile);
            log.info("Service registered successfully");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error registering service");
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateService(@RequestParam("title") String title,
                                           @RequestParam("description") String description,
                                           @RequestParam("price") Double price,
                                           @RequestParam("image") MultipartFile imageFile,
                                           @PathVariable("id") UUID id) {
        try {
            this.barberServiceService.updateService(title,description,price,imageFile, id);
            log.info("Service updated successfully with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error updating the service: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteService(@PathVariable("id") UUID id) {
        try {
            this.barberServiceService.deleteService(id);
            log.info("Service deleted successfully with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting the service: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
