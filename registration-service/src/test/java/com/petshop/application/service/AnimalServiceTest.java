package com.petshop.application.service;

import com.petshop.application.dto.AnimalDTO;
import com.petshop.domain.entity.Animal;
import com.petshop.domain.entity.Client;
import com.petshop.domain.exception.DuplicatedEntityException;
import com.petshop.domain.exception.NotFoundException;
import com.petshop.event.AnimalCreatedEvent;
import com.petshop.repository.AnimalRepository;
import com.petshop.repository.ClientRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
public class AnimalServiceTest {

    @Inject
    AnimalService animalService;

    @InjectMock
    AnimalRepository animalRepository;

    @InjectMock
    ClientRepository clientRepository;

    @Inject
    MockEmitters mockEmitters;

    @Test
    public void testCreateAnimal_Successful() {
        AnimalDTO dto = new AnimalDTO();
        dto.name = "Rex";
        dto.species = "Dog";
        dto.breed = "Pug";
        dto.clientId = 1L;

        Client mockClient = new Client();
        mockClient.id = 1L;

        when(clientRepository.findById(anyLong())).thenReturn(mockClient);
        when(animalRepository.findByNameAndClientId(anyString(), anyLong())).thenReturn(null);
        Mockito.doAnswer(invocation -> {
            Animal a = invocation.getArgument(0);
            a.id = 10L;
            return null;
        }).when(animalRepository).persist(any(Animal.class));

        AnimalDTO result = animalService.createAnimal(dto);

        Assertions.assertNotNull(result.id);
        Assertions.assertEquals(10L, result.id);
        verify(animalRepository).persist(any(Animal.class));
        verify(mockEmitters.animalEmitter()).send(any(AnimalCreatedEvent.class));
    }

    @Test
    public void testCreateAnimal_ClientNotFound() {
        AnimalDTO dto = new AnimalDTO();
        dto.clientId = 99L;

        when(clientRepository.findById(99L)).thenReturn(null);

        Assertions.assertThrows(NotFoundException.class, () -> {
            animalService.createAnimal(dto);
        });
    }

    @Test
    public void testCreateAnimal_DuplicateName() {
        AnimalDTO dto = new AnimalDTO();
        dto.name = "Rex";
        dto.clientId = 1L;

        when(clientRepository.findById(1L)).thenReturn(new Client());
        when(animalRepository.findByNameAndClientId("Rex", 1L)).thenReturn(new Animal());

        Assertions.assertThrows(DuplicatedEntityException.class, () -> {
            animalService.createAnimal(dto);
        });
    }
}
