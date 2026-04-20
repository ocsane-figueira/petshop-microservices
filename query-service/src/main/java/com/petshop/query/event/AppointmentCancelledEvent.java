package com.petshop.query.event;

public class AppointmentCancelledEvent {
    public Long appointmentId;

    public AppointmentCancelledEvent() {
    }

    public AppointmentCancelledEvent(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
}
