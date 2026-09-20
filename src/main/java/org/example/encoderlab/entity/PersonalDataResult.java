package org.example.encoderlab.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class PersonalDataResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mode;
    private String inputText;
    private String emails;
    private String phones;
    private String sanitizedText;
    private LocalDateTime createdAt;

    public PersonalDataResult() {
    }

    public PersonalDataResult(String mode, String inputText, List<String> emails,
                              List<String> phones, String sanitizedText) {
        this.mode = mode;
        this.inputText = inputText;
        this.emails = String.join(",", emails);
        this.phones = String.join(",", phones);
        this.sanitizedText = sanitizedText;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getMode() { return mode; }
    public String getInputText() { return inputText; }
    public String getEmails() { return emails; }
    public String getPhones() { return phones; }
    public String getSanitizedText() { return sanitizedText; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}