package com.petshop.query.domain.document;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection="animals")
public class AnimalDocument {
    public Long id; // ID originado no Postgres
    public String name;
    public Long clientId;
}
