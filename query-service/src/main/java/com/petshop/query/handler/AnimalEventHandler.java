package com.petshop.query.handler;

import com.petshop.query.domain.document.AnimalDocument;
import com.petshop.query.event.AnimalCreatedEvent;
import com.petshop.query.repository.AnimalDocumentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import io.vertx.core.json.JsonObject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AnimalEventHandler {
    
    private static final Logger LOG = Logger.getLogger(AnimalEventHandler.class);

    @Inject
    AnimalDocumentRepository animalRepository;

    @Incoming("animal-created")
    public void onAnimalCreated(JsonObject eventJson) {
        AnimalCreatedEvent event = eventJson.mapTo(AnimalCreatedEvent.class);
        
        AnimalDocument doc = new AnimalDocument();
        doc.id = event.animalId;
        doc.name = event.name;
        doc.clientId = event.clientId;
        
        animalRepository.persist(doc);
        LOG.info("CQRS: Animal " + doc.name + " salvo no MongoDB!");
    }
}
