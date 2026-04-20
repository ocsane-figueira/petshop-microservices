package com.petshop.application.event;

public class AppointmentCancelledEvent {
    private Long appointmentId;

    public AppointmentCancelledEvent() {
    }

    public AppointmentCancelledEvent(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
}
