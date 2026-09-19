package org.example.encoderlab.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonalDataControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String SAMPLE_ENG =
            "Contact me at john.doe@example.com or +375 (29) 123-45-67.";

    private static final String SAMPLE_CYRILLIC =
            "Свяжитесь со мной через john.doe@example.com или +375 (29) 123-45-67.";

    private URI uri(String text, String mode) {
        String q = "text=" + URLEncoder.encode(text, StandardCharsets.UTF_8)
                + "&mode=" + URLEncoder.encode(mode, StandardCharsets.UTF_8);
        return URI.create("http://localhost:" + port + "/api/v1/personal-data?" + q);
    }

    @Test
    void extract_shouldFindEmailAndPhone_eng() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                uri(SAMPLE_ENG, "extract"), String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody())
                .contains("\"emails\":[\"john.doe@example.com\"]")
                .contains("\"phones\":[\"+375 (29) 123-45-67\"]");
    }

    @Test
    void remove_shouldStripEmailAndPhone_eng() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                uri(SAMPLE_ENG, "remove"), String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody())
                .doesNotContain("john.doe@example.com")
                .doesNotContain("+375 (29) 123-45-67")
                .contains("Contact me at");
    }

    @Test
    void extract_shouldFindEmailAndPhone_cyr() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                uri(SAMPLE_CYRILLIC, "extract"), String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody())
                .contains("\"emails\":[\"john.doe@example.com\"]")
                .contains("\"phones\":[\"+375 (29) 123-45-67\"]");
    }

    @Test
    void remove_shouldStripEmailAndPhone_cyr() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                uri(SAMPLE_CYRILLIC, "remove"), String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody())
                .doesNotContain("john.doe@example.com")
                .doesNotContain("+375 (29) 123-45-67")
                .contains("Свяжитесь со мной через  или .");
    }
}