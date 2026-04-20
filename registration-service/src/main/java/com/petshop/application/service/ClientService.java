package com.petshop.application.service;

import com.petshop.application.dto.ClientDTO;
import com.petshop.domain.entity.Client;
import com.petshop.domain.exception.DuplicatedEntityException;
import com.petshop.event.ClientCreatedEvent;
import com.petshop.repository.ClientRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class ClientService {

    @Inject
    ClientRepository clientRepository;

    @Inject
    @Channel("client-created")
    Emitter<ClientCreatedEvent> clientCreatedEmitter;

    @Transactional
    public ClientDTO createClient(ClientDTO dto) {
        // Validação de Duplicidade (Regra de Negócio)
        if (clientRepository.findByCpf(dto.cpf) != null) {
            throw new DuplicatedEntityException("Já existe um cliente cadastrado com o CPF: " + dto.cpf);
        }

        // Mapeamento DTO -> Entidade
        Client client = new Client();
        client.name = dto.name;
        client.cpf = dto.cpf;
        client.email = dto.email;
        client.phone = dto.phone;

        // Persistência
        clientRepository.persist(client);

        // Emissão do Evento CQRS
        ClientCreatedEvent event = new ClientCreatedEvent(client.id, client.name, client.cpf);
        clientCreatedEmitter.send(event);

        // Mapeamento Entidade -> DTO
        dto.id = client.id;
        return dto;
    }
}
