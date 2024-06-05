package com.api.barber.services;

import com.api.barber.domain.entities.User;
import com.api.barber.domain.enums.UserRole;
import com.api.barber.domain.enums.UserStatus;
import com.api.barber.repositories.UserRepository;
import com.api.barber.rest.dtos.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto findUserByToken(Principal principal) {

        User user = this.userRepository.findByPhone(principal.getName());

        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
    }
    public void inactivateUserByToken(Principal principal) {
        this.userRepository.updateStatusByPhone(principal.getName(), UserStatus.INACTIVE);
    }
    public void checkIfUserExistsByPhone(String phone) {
        boolean existsUserByPhone = userRepository.existsByPhone(phone);

        if (existsUserByPhone) {
            throw new RuntimeException("User with this phone number already exists");
        }
    }

    public void checkIfUserIsInactive(User user, UserRole userRole) {
        if (isUserInactive(user)) {
            throw new RuntimeException(userRole.getRole().toLowerCase() + " is inactive");
        }
    }

    public boolean isUserInactive(User user) {
        return user.getStatus() == UserStatus.INACTIVE;
    }
}
