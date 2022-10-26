package com.sahil.webapp.repository;

import com.sahil.webapp.model.Document;
import com.sahil.webapp.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends CrudRepository<Document, UUID> {
    List<Document> findAllByUserAndStatus(User user, String status);
}
