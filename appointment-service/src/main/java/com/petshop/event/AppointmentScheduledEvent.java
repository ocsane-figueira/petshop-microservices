package com.petshop.event;

import java.time.LocalDateTime;

public class AppointmentScheduledEvent {
    public Long appointmentId;
    public Long animalId;
    public String type;
    public LocalDateTime startTime;
    public LocalDateTime endTime;

    public AppointmentScheduledEvent() {}

    public AppointmentScheduledEvent(Long appointmentId, Long animalId, String type, LocalDateTime startTime, LocalDateTime endTime) {
        this.appointmentId = appointmentId;
        this.animalId = animalId;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
