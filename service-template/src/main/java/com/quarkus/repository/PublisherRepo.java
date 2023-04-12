package com.quarkus.repository;

import com.quarkus.entity.Publisher;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

public class PublisherRepo implements PanacheRepository<Publisher> {
}
