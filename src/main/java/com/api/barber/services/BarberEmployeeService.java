package com.api.barber.services;

import com.api.barber.domain.entities.User;
import com.api.barber.domain.enums.UserRole;
import com.api.barber.domain.enums.UserStatus;
import com.api.barber.repositories.UserRepository;
import com.api.barber.rest.dtos.request.BarberEmployeeRequestDto;
import com.api.barber.rest.dtos.request.BarberEmployeeStatusRequestDto;
import com.api.barber.rest.dtos.response.BarberEmployeeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BarberEmployeeService {

    private final UserRepository userRepository;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final AuthService authService;

    public Page<BarberEmployeeResponseDto> findAllBarbers(Pageable pageable) {
        Page<User> barbers = this.userRepository.findAllByRole(pageable, UserRole.BARBER);

        return barbers.map(barber -> BarberEmployeeResponseDto.builder()
                .id(barber.getId())
                .firstName(barber.getFirstName())
                .lastName(barber.getLastName())
                .phone(barber.getPhone())
                .status(barber.getStatus())
                .build());
    }

    public Page<BarberEmployeeResponseDto> findAllActiveBarbers(Pageable pageable) {
        Page<User> barbers = this.userRepository.findByRoleInAndStatus(
                Set.of(UserRole.BARBER, UserRole.ADMIN),
                UserStatus.ACTIVE,
                pageable);

        return barbers.map(barber -> BarberEmployeeResponseDto.builder()
                .id(barber.getId())
                .firstName(barber.getFirstName())
                .lastName(barber.getLastName())
                .build());
    }

    public BarberEmployeeResponseDto findBarberById(UUID id, Principal principal) {

        User barber = this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("Barber not found"));

        User user = this.userRepository.findByPhone(principal.getName());

        boolean isAdmin = user.getRole().getRole().equals(UserRole.ADMIN.getRole());


        BarberEmployeeResponseDto.BarberEmployeeResponseDtoBuilder responseBuilder = BarberEmployeeResponseDto.builder()
                .id(barber.getId())
                .firstName(barber.getFirstName())
                .lastName(barber.getLastName());

        if (isAdmin) {
            responseBuilder.phone(barber.getPhone());
        }

        return responseBuilder.build();
    }

    public BarberEmployeeResponseDto saveBarber(BarberEmployeeRequestDto request) {

        this.userService.checkIfUserExistsByPhone(request.getPhone());

        this.authService.validatePasswordEquality(request.getPassword(), request.getConfirmPassword());

        User entityToSave = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .role(UserRole.BARBER)
                .status(UserStatus.ACTIVE)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User barberSaved = this.userRepository.save(entityToSave);

        return BarberEmployeeResponseDto.builder()
                .id(barberSaved.getId())
                .firstName(barberSaved.getFirstName())
                .lastName(barberSaved.getLastName())
                .phone(barberSaved.getPhone())
                .build();
    }

    public void updateBarberStatus(UUID barberId, BarberEmployeeStatusRequestDto request) throws Exception {
        User barber = userRepository.findById(barberId)
                .orElseThrow(() -> new RuntimeException("Barber not found"));

        if (barber.getRole() != UserRole.BARBER) {
            throw new Exception("User is not a barber");
        }

        barber.setStatus(request.getStatus());

        this.userRepository.save(barber);
    }
}
