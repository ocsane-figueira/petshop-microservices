package com.petshop.query.domain.document;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection="clients")
public class ClientDocument {
    public Long id; // ID originado no Postgres
    public String name;
    public String cpf;
}
