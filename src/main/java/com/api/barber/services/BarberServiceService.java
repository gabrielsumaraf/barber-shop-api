package com.api.barber.services;

import com.api.barber.domain.entities.BarberService;
import com.api.barber.repositories.BarberServiceRepository;
import com.api.barber.rest.dtos.response.ServiceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class BarberServiceService {

    private final BarberServiceRepository barberServiceRepository;
    public List<ServiceResponseDto> findAllServices() {
        return this.barberServiceRepository.findAll().stream().map(service ->
                ServiceResponseDto.builder()
                        .id(service.getId())
                        .title(service.getTitle())
                        .description(service.getDescription())
                        .price(service.getPrice())
                        .image(service.getImage())
                        .build()
        ).toList();
    }
    public void saveService(String title, String description,Double price, MultipartFile imageFile) throws IOException {

        byte[] imageData = imageFile.getBytes();

        BarberService barberService = BarberService.builder()
                .title(title)
                .description(description)
                .price(price)
                .image(imageData)
                .build();

        this.barberServiceRepository.save(barberService);
    }

    public void updateService(String title, String description,Double price, MultipartFile imageFile, UUID id) throws IOException {

       BarberService barberService = this.barberServiceRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("Barber service not found"));

        byte[] imageData = imageFile.getBytes();

        barberService.setTitle(title);
        barberService.setDescription(description);
        barberService.setPrice(price);
        barberService.setImage(imageData);

        this.barberServiceRepository.save(barberService);
    }
    public void deleteService(UUID id) {
        this.barberServiceRepository.deleteById(id);
    }
}
