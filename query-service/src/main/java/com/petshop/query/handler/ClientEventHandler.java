package com.petshop.query.handler;

import com.petshop.query.domain.document.ClientDocument;
import com.petshop.query.event.ClientCreatedEvent;
import com.petshop.query.repository.ClientDocumentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import io.vertx.core.json.JsonObject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ClientEventHandler {
    
    private static final Logger LOG = Logger.getLogger(ClientEventHandler.class);

    @Inject
    ClientDocumentRepository clientRepository;

    @Incoming("client-created")
    public void onClientCreated(JsonObject eventJson) {
        ClientCreatedEvent event = eventJson.mapTo(ClientCreatedEvent.class);
        
        ClientDocument doc = new ClientDocument();
        doc.id = event.getClientId();
        doc.name = event.getName();
        doc.cpf = event.getCpf();
        
        clientRepository.persist(doc);
        LOG.info("CQRS: Cliente " + doc.name + " salvo no MongoDB");
    }
}
