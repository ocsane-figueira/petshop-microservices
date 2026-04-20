package com.petshop.application.service;

import com.petshop.application.dto.AppointmentDTO;
import com.petshop.domain.entity.Appointment;
import com.petshop.domain.entity.AppointmentType;
import com.petshop.domain.exception.ActiveAppointmentExistsException;
import com.petshop.domain.exception.InvalidTimeException;
import com.petshop.domain.exception.NotFoundException;
import com.petshop.domain.exception.TimeOverlapException;
import com.petshop.event.AppointmentScheduledEvent;
import com.petshop.application.event.AppointmentCancelledEvent;
import com.petshop.repository.AppointmentRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
public class AppointmentServiceTest {

    @Inject
    AppointmentService appointmentService;

    @InjectMock
    AppointmentRepository appointmentRepository;

    @Inject
    MockEmitters mockEmitters;

    @Test
    public void testSchedule_Successful() {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setAnimalId(1L);
        dto.setType(AppointmentType.BANHO);
        dto.setStartTime(LocalDateTime.now().plusDays(1));
        dto.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

        when(appointmentRepository.hasOverlappingAppointment(any(), any())).thenReturn(false);
        when(appointmentRepository.hasActiveAppointmentForAnimal(anyLong(), any())).thenReturn(false);
        Mockito.doAnswer(invocation -> {
            Appointment a = invocation.getArgument(0);
            a.setId(100L);
            return null;
        }).when(appointmentRepository).persist(any(Appointment.class));

        AppointmentDTO result = appointmentService.schedule(dto);

        Assertions.assertNotNull(result.getId());
        verify(appointmentRepository).persist(any(Appointment.class));
        verify(mockEmitters.scheduledEmitter()).send(any(AppointmentScheduledEvent.class));
    }

    @Test
    public void testSchedule_InvalidTime() {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setStartTime(LocalDateTime.now().plusDays(1));
        dto.setEndTime(LocalDateTime.now().plusDays(1).minusHours(1));

        Assertions.assertThrows(InvalidTimeException.class, () -> {
            appointmentService.schedule(dto);
        });
    }

    @Test
    public void testSchedule_Overlap() {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setStartTime(LocalDateTime.now().plusDays(1));
        dto.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

        when(appointmentRepository.hasOverlappingAppointment(any(), any())).thenReturn(true);

        Assertions.assertThrows(TimeOverlapException.class, () -> {
            appointmentService.schedule(dto);
        });
    }

    @Test
    public void testSchedule_ActiveExists() {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setAnimalId(1L);
        dto.setStartTime(LocalDateTime.now().plusDays(1));
        dto.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

        when(appointmentRepository.hasOverlappingAppointment(any(), any())).thenReturn(false);
        when(appointmentRepository.hasActiveAppointmentForAnimal(anyLong(), any())).thenReturn(true);

        Assertions.assertThrows(ActiveAppointmentExistsException.class, () -> {
            appointmentService.schedule(dto);
        });
    }

    @Test
    public void testCancel_Successful() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);

        when(appointmentRepository.findById(1L)).thenReturn(appointment);

        appointmentService.cancelAppointment(1L);

        verify(appointmentRepository).delete(any(Appointment.class));
        verify(mockEmitters.cancelledEmitter()).send(any(com.petshop.application.event.AppointmentCancelledEvent.class));
    }

    @Test
    public void testCancel_NotFound() {
        when(appointmentRepository.findById(99L)).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> {
            appointmentService.cancelAppointment(99L);
        });
    }
}
