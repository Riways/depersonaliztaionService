package org.example.encoderlab.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BulkRequest(
        @NotEmpty(message = "texts must not be empty")
        @Size(max = 100, message = "texts must not exceed 100 items")
        List<String> texts,
        @Pattern(regexp = "extract|remove", message = "mode must be 'extract' or 'remove'")
        String mode
) {
}