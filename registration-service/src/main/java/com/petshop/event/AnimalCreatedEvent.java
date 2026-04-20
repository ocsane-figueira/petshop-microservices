package com.petshop.event;

public class AnimalCreatedEvent {
    public Long animalId;
    public String name;
    public Long clientId;

    public AnimalCreatedEvent() {}

    public AnimalCreatedEvent(Long animalId, String name, Long clientId) {
        this.animalId = animalId;
        this.name = name;
        this.clientId = clientId;
    }
}
