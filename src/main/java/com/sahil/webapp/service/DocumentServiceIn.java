package com.sahil.webapp.service;

import com.sahil.webapp.model.Document;
import com.sahil.webapp.model.User;

import java.util.List;
import java.util.UUID;

public interface DocumentServiceIn {

    Document save(Document doc);
    Document findById(UUID id);
    List<Document> findAll(User user);
}
