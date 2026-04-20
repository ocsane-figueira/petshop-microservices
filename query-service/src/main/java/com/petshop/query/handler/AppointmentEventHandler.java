package com.petshop.query.handler;

import com.petshop.query.domain.document.AnimalDocument;
import com.petshop.query.domain.document.AppointmentViewDocument;
import com.petshop.query.domain.document.ClientDocument;
import com.petshop.query.event.AppointmentScheduledEvent;
import com.petshop.query.event.AppointmentCancelledEvent;
import com.petshop.query.repository.AnimalDocumentRepository;
import com.petshop.query.repository.AppointmentViewRepository;
import com.petshop.query.repository.ClientDocumentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import io.vertx.core.json.JsonObject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AppointmentEventHandler {

    private static final Logger LOG = Logger.getLogger(AppointmentEventHandler.class);

    @Inject
    AppointmentViewRepository appointmentRepository;
    
    @Inject
    AnimalDocumentRepository animalRepository;
    
    @Inject
    ClientDocumentRepository clientRepository;

    @Incoming("appointment-scheduled")
    public void onAppointmentScheduled(JsonObject eventJson) {
        AppointmentScheduledEvent event = eventJson.mapTo(AppointmentScheduledEvent.class);
        
        // CQRS View Building: Junta dados soltos para otimizar leitura
        AnimalDocument animal = animalRepository.findByOriginalId(event.getAnimalId());
        String animalName = animal != null ? animal.name : "Desconhecido";
        Long clientId = animal != null ? animal.clientId : null;
        
        String clientName = "Desconhecido";
        if (clientId != null) {
            ClientDocument client = clientRepository.findByOriginalId(clientId);
            if (client != null) {
                clientName = client.name;
            }
        }
        
        AppointmentViewDocument doc = new AppointmentViewDocument();
        doc.appointmentId = event.getAppointmentId();
        doc.appointmentType = event.getType();
        doc.startTime = event.getStartTime();
        doc.endTime = event.getEndTime();
        doc.animalId = event.getAnimalId();
        doc.animalName = animalName;
        doc.clientId = clientId;
        doc.clientName = clientName;
        
        appointmentRepository.persist(doc);
        LOG.info("CQRS: View de Agendamento construída e salva no MongoDB!");
    }

    @Incoming("appointment-cancelled")
    public void onAppointmentCancelled(JsonObject eventJson) {
        AppointmentCancelledEvent event = eventJson.mapTo(AppointmentCancelledEvent.class);
        long deletedCount = appointmentRepository.delete("appointmentId", event.appointmentId);
        if (deletedCount > 0) {
            LOG.info("CQRS: View do Agendamento " + event.appointmentId + " apagada do MongoDB devido a cancelamento");
        }
    }
}
