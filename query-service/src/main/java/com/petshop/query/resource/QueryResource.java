package com.petshop.query.resource;

import com.petshop.query.domain.document.AppointmentViewDocument;
import com.petshop.query.domain.document.ClientDocument;
import com.petshop.query.repository.AppointmentViewRepository;
import com.petshop.query.repository.ClientDocumentRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/query")
@Produces(MediaType.APPLICATION_JSON)
public class QueryResource {

    @Inject
    AppointmentViewRepository appointmentRepository;

    @Inject
    ClientDocumentRepository clientRepository;

    @GET
    @Path("/appointments")
    public List<AppointmentViewDocument> listAllAppointments() {
        // Retorno do mongoDB sem necessiadade de Join
        return appointmentRepository.listAll();
    }

    @GET
    @Path("/clients")
    public List<ClientDocument> listAllClients() {
        return clientRepository.listAll();
    }
}
