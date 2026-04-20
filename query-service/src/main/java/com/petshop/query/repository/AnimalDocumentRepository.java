package com.petshop.query.repository;

import com.petshop.query.domain.document.AnimalDocument;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AnimalDocumentRepository implements PanacheMongoRepository<AnimalDocument> {
    public AnimalDocument findByOriginalId(Long id) {
        return find("_id", id).firstResult();
    }
}
