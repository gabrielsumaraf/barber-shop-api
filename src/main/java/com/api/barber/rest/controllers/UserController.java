package com.api.barber.rest.controllers;

import com.api.barber.rest.dtos.response.UserResponseDto;
import com.api.barber.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserResponseDto findUserByToken(Principal principal) {
        try {
            UserResponseDto responseDto = this.userService.findUserByToken(principal);
            log.info("User fetched successfully");
            return responseDto;
        } catch (Exception e) {
            log.error("Error fetching user {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/inactivate")
    public ResponseEntity<Void> inactivateUserByToken(Principal principal) {
        try {
            this.userService.inactivateUserByToken(principal);
            log.info("User inactivated successfully");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error inactivating user {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
