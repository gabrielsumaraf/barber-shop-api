package com.api.barber.services;

import com.api.barber.domain.entities.Appointment;
import com.api.barber.domain.entities.BarberService;
import com.api.barber.domain.entities.User;
import com.api.barber.domain.entities.WorkingHour;
import com.api.barber.domain.enums.AppointmentStatus;
import com.api.barber.domain.enums.UserRole;
import com.api.barber.repositories.AppointmentRepository;
import com.api.barber.repositories.BarberServiceRepository;
import com.api.barber.repositories.UserRepository;
import com.api.barber.repositories.WorkingHourRepository;
import com.api.barber.rest.dtos.request.AppointmentRequestDto;
import com.api.barber.rest.dtos.request.AppointmentStatusUpdateRequestDto;
import com.api.barber.rest.dtos.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final UserService userService;
    private final UserRepository userRepository;

    private final BarberServiceRepository barberServiceRepository;

    private final WorkingHourRepository workingHourRepository;

    private final AppointmentRepository appointmentRepository;

    public Page<BarberAppointmentResponseDto> findAppointmentsByBarberId(Principal principal,
                                                                         AppointmentStatus status,
                                                                         LocalDate startDate,
                                                                         LocalDate endDate,
                                                                         Pageable pageable) {
        User user = this.userRepository.findByPhone(principal.getName());

        Page<Appointment> appointments = appointmentRepository.findByBarberIdAndStatusAndDateBetween(
                user.getId(),
                status != null ? status.getValue() : null,
                startDate,
                endDate,
                pageable
        );

        return appointments.map(appointment -> BarberAppointmentResponseDto.builder()
                .id(appointment.getId())
                .customerName(appointment.getCustomer().getFirstName() + " " + appointment.getCustomer().getLastName())
                .customerPhone(appointment.getCustomer().getPhone())
                .date(appointment.getDate())
                .hour(appointment.getWorkingHour().getHourOfDay())
                .barberServiceTitle(appointment.getBarberService().getTitle())
                .total(appointment.getTotal())
                .status(appointment.getStatus())
                .build());
    }

    public Page<CustomerAppointmentResponseDto> findAppointmentsByCustomerId(Principal principal,
                                                                             AppointmentStatus status,
                                                                             LocalDate startDate,
                                                                             LocalDate endDate,
                                                                             Pageable pageable) {
        User user = this.userRepository.findByPhone(principal.getName());
        Page<Appointment> appointments = this.appointmentRepository.findByCustomerIdAndStatusAndDateBetween(
                user.getId(),
                status != null ? status.getValue() : null,
                startDate,
                endDate,
                pageable
        );

        return appointments.map(appointment -> CustomerAppointmentResponseDto.builder()
                .id(appointment.getId())
                .barberName(appointment.getBarber().getFirstName() + " " + appointment.getBarber().getLastName())
                .date(appointment.getDate())
                .hour(appointment.getWorkingHour().getHourOfDay())
                .barberServiceTitle(appointment.getBarberService().getTitle())
                .total(appointment.getTotal())
                .status(appointment.getStatus())
                .build());
    }

    public Page<OwnerAppointmentResponseDto> findAllAppointments(AppointmentStatus status,
                                                                 LocalDate startDate,
                                                                 LocalDate endDate,
                                                                 Pageable pageable) {

        Page<Appointment> appointments = this.appointmentRepository.findAllByStatusAndDateBetween(
                status != null ? status.getValue() : null,
                startDate,
                endDate,
                pageable
        );

        return appointments.map(appointment -> OwnerAppointmentResponseDto.builder()
                .id(appointment.getId())
                .customerName(appointment.getCustomer().getFirstName() + " " + appointment.getCustomer().getLastName())
                .customerPhone(appointment.getCustomer().getPhone())
                .barberName(appointment.getBarber().getFirstName() + " " + appointment.getBarber().getLastName())
                .barberPhone(appointment.getBarber().getPhone())
                .barberServiceTitle(appointment.getBarberService().getTitle())
                .date(appointment.getDate())
                .hour(appointment.getWorkingHour().getHourOfDay())
                .total(appointment.getTotal())
                .status(appointment.getStatus())
                .build());
    }

    public List<WorkingHourResponseDto> findAvailableHours(LocalDate date, UUID barberId) throws Exception {

        this.isDateBeforeNow(date);
        User barber = this.userRepository.findById(barberId).orElseThrow(() -> new RuntimeException("Barber not found"));

        this.userService.checkIfUserIsInactive(barber, barber.getRole());

        String dayOfWeek = date.getDayOfWeek().toString();
        List<WorkingHour> workingAvailableHours = workingHourRepository.findAllByDateAndDayOfWeek(date, dayOfWeek, barberId);

        return workingAvailableHours.stream().map(workingHour -> WorkingHourResponseDto.builder()
                .id(workingHour.getId())
                .hourOfDay(workingHour.getHourOfDay())
                .build()).toList();
    }

    public CustomerAppointmentResponseDto saveAppointment(Principal principal, AppointmentRequestDto request) throws Exception {

        this.isDateBeforeNow(request.getDate());

        User barber = userRepository.findById(request.getBarberId())
                .orElseThrow(() -> new RuntimeException("Barber not found"));

        this.userService.checkIfUserIsInactive(barber, barber.getRole());

        if (barber.getRole() == UserRole.CUSTOMER) {
            throw new RuntimeException("User is not a barber");
        }

        User customer = userRepository.findByPhone(principal.getName());

        if (customer.getCustomerAppointments().stream()
                .filter(appointment -> appointment.getStatus() == AppointmentStatus.PENDING)
                .count() >= 3) {
            throw new RuntimeException("The customer has more than 3 pending appointments");
        }
        BarberService barberService = barberServiceRepository.findById(request.getBarberServiceId())
                .orElseThrow(() -> new RuntimeException("Barber service not found"));

        WorkingHour workingHour = workingHourRepository.findById(request.getWorkingHourId())
                .orElseThrow(() -> new RuntimeException("Working hour not found"));

        String requestDay = String.valueOf(request.getDate().getDayOfWeek());

        if (!workingHour.getDayOfWeek().equals(requestDay)) {
            throw new Exception("The appointment day does not match the working day.");
        }
        boolean exists = this.appointmentRepository.existsByBarberIdAndCustomerIdAndWorkingHourIdAndBarberServiceIdAndDateAndStatus(
                request.getBarberId(),
                customer.getId(),
                workingHour.getId(),
                barberService.getId(),
                request.getDate(),
                AppointmentStatus.PENDING);
        if (exists) {
            throw new RuntimeException("There is already an appointment for the same parameters.");
        }
        Appointment appointment = Appointment.builder()
                .barber(barber)
                .customer(customer)
                .workingHour(workingHour)
                .barberService(barberService)
                .status(AppointmentStatus.PENDING)
                .date(request.getDate())
                .createdAt(LocalDateTime.now())
                .total(barberService.getPrice())
                .build();

        Appointment appointmentSaved = this.appointmentRepository.save(appointment);

        return CustomerAppointmentResponseDto.builder()
                .id(appointment.getId())
                .barberName(appointment.getBarber().getFirstName() + " " + appointment.getBarber().getLastName())
                .date(appointment.getDate())
                .hour(appointment.getWorkingHour().getHourOfDay())
                .barberServiceTitle(appointment.getBarberService().getTitle())
                .total(appointment.getTotal())
                .status(appointment.getStatus())
                .build();
    }

    public AppointmentStatusUpdateResponseDto updateAppointmentStatus(Principal principal, UUID id, AppointmentStatusUpdateRequestDto request)
            throws Exception {
        User user = this.userRepository.findByPhone(principal.getName());

        Appointment appointment = this.appointmentRepository.findById(id)
                .orElseThrow(() -> new Exception("Appointment not found"));

        if (user.getRole() == UserRole.CUSTOMER) {
            if (!user.getId().equals(appointment.getCustomer().getId())) {
                throw new Exception("Customers can only cancel their own appointments.");
            }

            this.validateAppointmentStatus(appointment.getStatus());

            if (!request.getAppointmentStatus().getValue().equals(AppointmentStatus.CANCELED.getValue())) {
                throw new Exception("Customers can only cancel appointments");
            }
        } else if (user.getRole() == UserRole.BARBER) {
            if (!user.getId().equals(appointment.getBarber().getId())) {
                throw new Exception("Unauthorized to update appointment status");
            }
            this.validateAppointmentStatus(appointment.getStatus());

            if (request.getAppointmentStatus() == AppointmentStatus.PENDING) {
                throw new Exception("Barbers can't set pending appointment");
            }
        }
        appointment.setStatus(request.getAppointmentStatus());

        this.appointmentRepository.save(appointment);

        return AppointmentStatusUpdateResponseDto.builder()
                .message("Appointment status updated successfully")
                .build();
    }

    private void validateAppointmentStatus(AppointmentStatus appointmentStatus) throws Exception {
        if (appointmentStatus != AppointmentStatus.PENDING) {
            throw new Exception("Appointment is not pending");
        }
    }

    private void isDateBeforeNow(LocalDate date) throws Exception {
        if (date.isBefore(LocalDate.now())) {
            throw new Exception("The provided date is before the current date");
        }
    }
}
