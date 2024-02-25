package com.api.barber.services;

import com.api.barber.domain.entities.User;
import com.api.barber.domain.enums.UserRole;
import com.api.barber.repositories.UserRepository;
import com.api.barber.rest.dtos.request.LoginRequestDto;
import com.api.barber.rest.dtos.request.RegisterRequestDto;
import com.api.barber.rest.dtos.response.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    public void register(RegisterRequestDto request) {

        this.checkIfUserExistsByPhone(request.getPhone());

        this.validatePasswordEquality(request.getPassword(), request.getConfirmPassword());

        User entityToSave = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.CUSTOMER)
                .build();

        userRepository.save(entityToSave);
    }

    private void validatePasswordEquality(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)){
            throw new RuntimeException("Passwords do not match");
        }
    }

    public LoginResponseDto login(LoginRequestDto request) {

        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
                request.getPhone(),
                request.getPassword()
        );

        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return LoginResponseDto.builder()
                .token(token)
                .build();
    }

    private void checkIfUserExistsByPhone(String phone) {

        boolean existsUserByPhone = userRepository.existsByPhone(phone);

        if (existsUserByPhone) {
            throw new RuntimeException("User with this phone number already exists");
        }
    }
}
