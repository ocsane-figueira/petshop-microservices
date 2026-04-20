package com.petshop.application.service;

import com.petshop.application.dto.AppointmentDTO;
import com.petshop.domain.entity.Appointment;
import com.petshop.domain.exception.InvalidTimeException;
import com.petshop.domain.exception.TimeOverlapException;
import com.petshop.domain.exception.ActiveAppointmentExistsException;
import com.petshop.event.AppointmentScheduledEvent;
import com.petshop.application.event.AppointmentCancelledEvent;
import com.petshop.repository.AppointmentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import java.time.LocalDateTime;
import com.petshop.domain.exception.NotFoundException;

@ApplicationScoped
public class AppointmentService {

    @Inject
    AppointmentRepository appointmentRepository;

    @Inject
    @Channel("appointment-scheduled")
    Emitter<AppointmentScheduledEvent> emitter;

    @Inject
    @Channel("appointment-cancelled")
    Emitter<AppointmentCancelledEvent> cancelEmitter;

    @Transactional
    public AppointmentDTO schedule(AppointmentDTO dto) {
        // Data de término deve ser posterior a data de início
        if (dto.getEndTime().isBefore(dto.getStartTime()) || dto.getEndTime().isEqual(dto.getStartTime())) {
            throw new InvalidTimeException("O horário de término deve ser posterior ao horário de início.");
        }

        // Não pode permitir sobreposição de agendamentos
        if (appointmentRepository.hasOverlappingAppointment(dto.getStartTime(), dto.getEndTime())) {
            throw new TimeOverlapException("Já existe um agendamento conflitante neste horário no Petshop.");
        }

        // Só pode ter 1 agendamento ativo por vez
        if (appointmentRepository.hasActiveAppointmentForAnimal(dto.getAnimalId(), LocalDateTime.now())) {
            throw new ActiveAppointmentExistsException("O animal já possui um agendamento ativo futuro. Conclua ou cancele o atual antes de agendar outro.");
        }

        Appointment appointment = new Appointment();
        appointment.setAnimalId(dto.getAnimalId());
        appointment.setType(dto.getType());
        appointment.setStartTime(dto.getStartTime());
        appointment.setEndTime(dto.getEndTime());

        appointmentRepository.persist(appointment);

        AppointmentScheduledEvent event = new AppointmentScheduledEvent(
                appointment.getId(),
                appointment.getAnimalId(),
                appointment.getType().name(),
                appointment.getStartTime(),
                appointment.getEndTime()
        );
        
        emitter.send(event);
        dto.setId(appointment.getId());
        
        return dto;
    }

    @Transactional
    public void cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id);
        if (appointment == null) {
            throw new NotFoundException("Agendamento não encontrado.");
        }
        
        appointmentRepository.delete(appointment);
        
        cancelEmitter.send(new AppointmentCancelledEvent(id));
    }
}
