package com.petshop.query.domain.document;

import io.quarkus.mongodb.panache.common.MongoEntity;
import java.time.LocalDateTime;

@MongoEntity(collection="appointments_view")
public class AppointmentViewDocument {
    public Long appointmentId;
    public String appointmentType;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    
    // Campos Desnormalizados (A magia do CQRS)
    public Long animalId;
    public String animalName;
    public Long clientId;
    public String clientName;
}
