package com.sahil.webapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "document")
public class Document {

    @Id
    @Column(name="doc_id")
    @JsonProperty("doc_id")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    private UUID docId;

    @JsonProperty("name")
    @Column(name="name")
    private String name;

    @JsonProperty("s3_bucket_path")
    @Column(name="s3_bucket_path")
    private String s3BucketPath;

    @JsonProperty("date_created")
    @Column(name="date_created")
    private Timestamp documentCreated;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(name="status")
    @JsonIgnore
    private String status;

    public String getStatus() {
        return status;
    }

    public UUID getDocId() {
        return docId;
    }

    public String getName() {
        return name;
    }

    public String getS3BucketPath() {
        return s3BucketPath;
    }

    public Timestamp getDocumentCreated() {
        return documentCreated;
    }

    public User getUser() {
        return user;
    }

    public void setDocId(UUID docId) {
        this.docId = docId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setS3BucketPath(String s3BucketPath) {
        this.s3BucketPath = s3BucketPath;
    }

    public void setDocumentCreated(Timestamp documentCreated) {
        this.documentCreated = documentCreated;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
