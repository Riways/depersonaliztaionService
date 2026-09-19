package org.example.encoderlab.dto;

import java.util.List;

public record PersonalDataResponse(
        List<String> emails,
        List<String> phones,
        String sanitizedText
) {
}