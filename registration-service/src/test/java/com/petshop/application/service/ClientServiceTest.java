package com.petshop.application.service;

import com.petshop.application.dto.ClientDTO;
import com.petshop.domain.entity.Client;
import com.petshop.domain.exception.DuplicatedEntityException;
import com.petshop.event.ClientCreatedEvent;
import com.petshop.repository.ClientRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
public class ClientServiceTest {

    @Inject
    ClientService clientService;

    @InjectMock
    ClientRepository clientRepository;

    @Inject
    MockEmitters mockEmitters;

    @Test
    public void testCreateClient_Successful() {
        ClientDTO dto = new ClientDTO();
        dto.name = "Test Client";
        dto.cpf = "12345678901";
        dto.email = "test@client.com";
        dto.phone = "123456789";

        when(clientRepository.findByCpf(dto.cpf)).thenReturn(null);
        Mockito.doAnswer(invocation -> {
            Client c = invocation.getArgument(0);
            c.id = 1L;
            return null;
        }).when(clientRepository).persist(any(Client.class));

        ClientDTO result = clientService.createClient(dto);

        Assertions.assertNotNull(result.id);
        Assertions.assertEquals(1L, result.id);
        verify(clientRepository).persist(any(Client.class));
        verify(mockEmitters.clientEmitter()).send(any(ClientCreatedEvent.class));
    }

    @Test
    public void testCreateClient_DuplicateCpf() {
        ClientDTO dto = new ClientDTO();
        dto.cpf = "12345678901";

        when(clientRepository.findByCpf(dto.cpf)).thenReturn(new Client());

        Assertions.assertThrows(DuplicatedEntityException.class, () -> {
            clientService.createClient(dto);
        });
    }
}
