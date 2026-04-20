package com.petshop.repository;

import com.petshop.domain.entity.Client;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ClientRepository implements PanacheRepository<Client> {
    public Client findByCpf(String cpf) {
        return find("cpf", cpf).firstResult();
    }
}
