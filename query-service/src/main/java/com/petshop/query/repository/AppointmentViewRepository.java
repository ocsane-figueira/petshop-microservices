package com.petshop.query.repository;

import com.petshop.query.domain.document.AppointmentViewDocument;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AppointmentViewRepository implements PanacheMongoRepository<AppointmentViewDocument> {}
