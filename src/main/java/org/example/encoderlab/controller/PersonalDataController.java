package org.example.encoderlab.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.example.encoderlab.dto.PersonalDataResponse;
import org.example.encoderlab.service.PersonalDataService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/personal-data")
@Validated
public class PersonalDataController {

    private final PersonalDataService pds;

    public PersonalDataController(PersonalDataService pds) {
        this.pds = pds;
    }

    @GetMapping
    public PersonalDataResponse process(
            @RequestParam
            @NotBlank(message = "text must not be blank")
            @Size(max = 10000, message = "text must not exceed 10000 characters")
            String text,
            @RequestParam
            @Pattern(regexp = "extract|remove", message = "mode must be 'extract' or 'remove'")
            String mode
    ) {
        log.info("Request mode={}, length={}", mode, text.length());
        return pds.process(text, mode);
    }
}