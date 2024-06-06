package com.api.barber.services;

import com.api.barber.domain.entities.BarberService;
import com.api.barber.domain.entities.User;
import com.api.barber.domain.enums.BarberServiceStatus;
import com.api.barber.domain.enums.UserRole;
import com.api.barber.repositories.BarberServiceRepository;
import com.api.barber.rest.dtos.request.BarberServiceStatusRequestDto;
import com.api.barber.rest.dtos.response.BarberServiceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BarberServiceService {

    private final BarberServiceRepository barberServiceRepository;

    public Page<BarberServiceResponseDto> findAllActiveStatus(Pageable pageable) {
        return this.barberServiceRepository.findAllByStatus(BarberServiceStatus.ACTIVE, pageable).map(service ->
                BarberServiceResponseDto.builder()
                        .id(service.getId())
                        .title(service.getTitle())
                        .description(service.getDescription())
                        .price(service.getPrice())
                        .image(service.getImage())
                        .status(service.getStatus())
                        .build()
        );
    }

    public Page<BarberServiceResponseDto> findAll(Pageable pageable) {
        return this.barberServiceRepository.findAll(pageable).map(service ->
                BarberServiceResponseDto.builder()
                        .id(service.getId())
                        .title(service.getTitle())
                        .description(service.getDescription())
                        .price(service.getPrice())
                        .image(service.getImage())
                        .status(service.getStatus())
                        .build()
        );
    }

    public BarberServiceResponseDto findById(UUID id) {
        BarberService service = this.barberServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barber service not found"));

        return BarberServiceResponseDto.builder()
                .id(service.getId())
                .title(service.getTitle())
                .description(service.getDescription())
                .price(service.getPrice())
                .image(service.getImage())
                .status(service.getStatus())
                .build();
    }

    public BarberServiceResponseDto saveService(String title, String description, Double price, MultipartFile imageFile) throws IOException {

        BarberService.BarberServiceBuilder barberServiceBuilder = BarberService.builder()
                .title(title)
                .description(description)
                .price(price)
                .status(BarberServiceStatus.ACTIVE);

        if (imageFile != null){
            byte[] imageData = imageFile.getBytes();
            barberServiceBuilder.image(imageData);
        }
        BarberService barberServiceSaved = this.barberServiceRepository.save(barberServiceBuilder.build());

        return BarberServiceResponseDto.builder()
                .id(barberServiceSaved.getId())
                .title(barberServiceSaved.getTitle())
                .description(barberServiceSaved.getDescription())
                .price(barberServiceSaved.getPrice())
                .image(barberServiceSaved.getImage())
                .status(barberServiceSaved.getStatus())
                .build();
    }

    public void updateService(String title, String description, Double price, MultipartFile imageFile, UUID id) throws IOException {

        BarberService barberService = this.barberServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barber service not found"));

        if (imageFile != null){
            byte[] imageData = imageFile.getBytes();
            barberService.setImage(imageData);
        }

        barberService.setTitle(title);
        barberService.setDescription(description);
        barberService.setPrice(price);

        this.barberServiceRepository.save(barberService);
    }

    public void updateServiceStatus(UUID id, BarberServiceStatusRequestDto request) {
        BarberService barberService = barberServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barber service not found"));

        barberService.setStatus(request.getStatus());

        this.barberServiceRepository.save(barberService);
    }
}
