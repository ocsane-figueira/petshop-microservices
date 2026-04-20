package com.petshop.repository;

import com.petshop.domain.entity.Appointment;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class AppointmentRepository implements PanacheRepository<Appointment> {
    

    public boolean hasOverlappingAppointment(LocalDateTime newStart, LocalDateTime newEnd) {
        long count = count("startTime < ?2 and endTime > ?1", newStart, newEnd);
        return count > 0;
    }

    public boolean hasActiveAppointmentForAnimal(Long animalId, LocalDateTime now) {
        long count = count("animalId = ?1 and endTime > ?2", animalId, now);
        return count > 0;
    }
}
