package com.api.barber.services;

import com.api.barber.domain.entities.User;
import com.api.barber.domain.enums.UserRole;
import com.api.barber.domain.enums.UserStatus;
import com.api.barber.repositories.UserRepository;
import com.api.barber.rest.dtos.request.LoginRequestDto;
import com.api.barber.rest.dtos.request.RegisterRequestDto;
import com.api.barber.rest.dtos.response.LoginResponseDto;
import com.api.barber.rest.dtos.response.UserResponseDto;
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

    private final UserService userService;

    public void register(RegisterRequestDto request) {

        this.userService.checkIfUserExistsByPhone(request.getPhone());

        this.validatePasswordEquality(request.getPassword(), request.getConfirmPassword());

        User entityToSave = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.CUSTOMER)
                .status(UserStatus.ACTIVE)
                .build();

        this.userRepository.save(entityToSave);
    }

    public LoginResponseDto login(LoginRequestDto request) {


        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
                request.getPhone(),
                request.getPassword()
        );

        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        User user = this.userRepository.findByPhone(request.getPhone());

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();

        return LoginResponseDto.builder()
                .user(userResponseDto)
                .token(token)
                .build();
    }

    public void validatePasswordEquality(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }
    }
}
