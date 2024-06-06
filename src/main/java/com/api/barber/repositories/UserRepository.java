package com.api.barber.repositories;

import com.api.barber.domain.entities.User;
import com.api.barber.domain.enums.UserRole;
import com.api.barber.domain.enums.UserStatus;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User findByPhone(String phone);

    boolean existsByPhone(String phone);

    Page<User> findByRoleInAndStatus(Set<UserRole> barber, UserStatus userStatus, Pageable pageable);

    @Query("UPDATE users u SET u.status = :status WHERE u.phone = :phone")
    @Transactional
    @Modifying
    void updateStatusByPhone(@NonNull @Param("phone") String phone, @NonNull @Param("status") UserStatus status);

    Page<User> findAllByRole(Pageable pageable, UserRole userRole);
}
