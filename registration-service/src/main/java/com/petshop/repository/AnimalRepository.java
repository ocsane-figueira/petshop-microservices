package com.petshop.repository;

import com.petshop.domain.entity.Animal;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AnimalRepository implements PanacheRepository<Animal> {
    public Animal findByNameAndClientId(String name, Long clientId) {
        return find("name = ?1 and client.id = ?2", name, clientId).firstResult();
    }
}
