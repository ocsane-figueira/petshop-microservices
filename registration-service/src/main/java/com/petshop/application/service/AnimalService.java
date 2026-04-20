package com.petshop.application.service;

import com.petshop.application.dto.AnimalDTO;
import com.petshop.domain.entity.Animal;
import com.petshop.domain.entity.Client;
import com.petshop.domain.exception.DuplicatedEntityException;
import com.petshop.domain.exception.NotFoundException;
import com.petshop.event.AnimalCreatedEvent;
import com.petshop.repository.AnimalRepository;
import com.petshop.repository.ClientRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class AnimalService {

    @Inject
    AnimalRepository animalRepository;

    @Inject
    ClientRepository clientRepository;

    @Inject
    @Channel("animal-created")
    Emitter<AnimalCreatedEvent> animalCreatedEmitter;

    @Transactional
    public AnimalDTO createAnimal(AnimalDTO dto) {
        // Validação: Cliente existe?
        Client client = clientRepository.findById(dto.clientId);
        if (client == null) {
            throw new NotFoundException("Cliente com ID " + dto.clientId + " não encontrado.");
        }

        // Validação de Duplicidade: Mesmo nome para o mesmo cliente?
        if (animalRepository.findByNameAndClientId(dto.name, dto.clientId) != null) {
            throw new DuplicatedEntityException("O cliente já possui um animal cadastrado com o nome: " + dto.name);
        }

        // Mapeamento DTO -> Entidade
        Animal animal = new Animal();
        animal.name = dto.name;
        animal.species = dto.species;
        animal.breed = dto.breed;
        animal.client = client;

        // Persistência
        animalRepository.persist(animal);

        // Emissão do Evento CQRS
        AnimalCreatedEvent event = new AnimalCreatedEvent(animal.id, animal.name, client.id);
        animalCreatedEmitter.send(event);

        // Mapeamento Entidade -> DTO
        dto.id = animal.id;
        return dto;
    }
}
