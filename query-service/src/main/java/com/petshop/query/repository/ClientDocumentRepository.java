package com.petshop.query.repository;

import com.petshop.query.domain.document.ClientDocument;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ClientDocumentRepository implements PanacheMongoRepository<ClientDocument> {
    public ClientDocument findByOriginalId(Long id) {
        return find("_id", id).firstResult();
    }
}
