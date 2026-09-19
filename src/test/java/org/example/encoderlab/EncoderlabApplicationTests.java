package org.example.encoderlab;

import org.example.encoderlab.service.PersonalDataService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EncoderlabApplicationTests {

    private final PersonalDataService pdc = new PersonalDataService();

    @Test
    void extractEmails_shouldFindSingleEmail() {
        String text = "Contact me at john.doe@example.com";
        List<String> emails = pdc.extractEmails(text);
        assertEquals(1, emails.size());
        assertEquals("john.doe@example.com", emails.getFirst());
    }
    @Test
    void extractEmails_shouldFindTwoEmails() {
        String text = "Contact me at john.doe@example.com or john.doe2@example.com";
        List<String> emails = pdc.extractEmails(text);
        assertEquals(2, emails.size());
        assertEquals("john.doe@example.com", emails.get(0));
        assertEquals("john.doe2@example.com", emails.get(1));
    }
    @Test
    void extractEmails_shouldFindTwoEqualEmailsAsOne() {
        String text = "Contact me at john.doe@example.com or john.doe@example.com";
        List<String> emails = pdc.extractEmails(text);
        assertEquals(1, emails.size());
        assertEquals("john.doe@example.com", emails.getFirst());
    }
    @Test
    void extractEmails_shouldReturnEmptyListWhenNoEmails() {
        String text = "Hello, world!";
        List<String> emails = pdc.extractEmails(text);
        assertTrue(emails.isEmpty());
    }
    @Test
    void extractEmails_nullInputReturnsEmptyString(){
        assertThrows(IllegalArgumentException.class, ()-> pdc.extractEmails(null));
    }
}
