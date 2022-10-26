package com.sahil.webapp.service;

import com.sahil.webapp.model.Document;
import com.sahil.webapp.model.User;
import com.sahil.webapp.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentService implements DocumentServiceIn{

    @Autowired
    DocumentRepository documentRepository;

    @Override
    public Document save(Document doc) {
        return documentRepository.save(doc);
    }

    @Override
    public Document findById(UUID id) {
       Document doc=  documentRepository.findById(id).get();
       return doc;
    }

    @Override
    public List<Document> findAll(User user) {
        List<Document> doc=  documentRepository.findAllByUserAndStatus(user, "ACTIVE");
        return doc;
    }
}
