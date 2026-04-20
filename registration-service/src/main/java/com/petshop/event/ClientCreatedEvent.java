package com.petshop.event;

public class ClientCreatedEvent {
    public Long clientId;
    public String name;
    public String cpf;

    public ClientCreatedEvent() {}

    public ClientCreatedEvent(Long clientId, String name, String cpf) {
        this.clientId = clientId;
        this.name = name;
        this.cpf = cpf;
    }
}
