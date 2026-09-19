package org.example.encoderlab.controller;

import org.example.encoderlab.dto.PersonalDataResponse;
import org.example.encoderlab.service.PersonalDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/personal-data")
public class PersonalDataController {
    private final PersonalDataService pds;

    public PersonalDataController(PersonalDataService pds) {
        this.pds = pds;
    }

    @GetMapping
    public PersonalDataResponse process(
            @RequestParam String text,
            @RequestParam String mode
    ) {
        return switch (mode) {
            case "extract" -> new PersonalDataResponse(pds.extractEmails(text), pds.extractPhones(text), "");
            case "remove" -> new PersonalDataResponse(List.of(), List.of(), pds.removeAll(text));
            default -> throw new IllegalArgumentException("Unknown mode: " + mode);
        };

    }
}
