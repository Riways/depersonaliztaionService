package org.example.encoderlab.service;

import org.example.encoderlab.cache.PersonalDataCache;
import org.example.encoderlab.counter.RequestCounter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersonalDataServiceTest {
    private final PersonalDataService service = new PersonalDataService(new PersonalDataCache(), new RequestCounter());

    // ==================================================
    // extractEmails
    // ==================================================

    @Test
    void extractEmails_shouldThrowOnNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.extractEmails(null));
    }

    @Test
    void extractEmails_shouldReturnEmptyListForEmptyString() {
        assertTrue(service.extractEmails("").isEmpty());
    }

    @Test
    void extractEmails_shouldReturnEmptyListWhenNoEmails() {
        assertTrue(service.extractEmails("Hello, world!").isEmpty());
    }

    @Test
    void extractEmails_shouldFindSingleEmail() {
        List<String> emails = service.extractEmails("Contact me at john.doe@example.com");
        assertEquals(List.of("john.doe@example.com"), emails);
    }

    @Test
    void extractEmails_shouldFindTwoDistinctEmails() {
        List<String> emails = service.extractEmails("a@b.com and c@d.org");
        assertEquals(List.of("a@b.com", "c@d.org"), emails);
    }

    @Test
    void extractEmails_shouldReturnUniqueEmails() {
        List<String> emails = service.extractEmails("a@b.com and a@b.com");
        assertEquals(List.of("a@b.com"), emails);
    }

    @Test
    void extractEmails_shouldPreserveOrder() {
        List<String> emails = service.extractEmails("c@d.org then a@b.com");
        assertEquals(List.of("c@d.org", "a@b.com"), emails);
    }

    @Test
    void extractEmails_shouldReturnImmutableList() {
        List<String> emails = service.extractEmails("a@b.com");
        assertThrows(UnsupportedOperationException.class,
                () -> emails.add("x@y.com"));
    }

    @Test
    void extractEmails_shouldIgnoreInvalidEmailLikeString() {
        // нет точки в домене — не email
        assertTrue(service.extractEmails("not-an-email@localhost").isEmpty());
    }

    @Test
    void extractEmails_shouldNotMatchInsideLongerToken() {
        // границы regex не идеальны, но проверим очевидное
        List<String> emails = service.extractEmails("prefix a@b.com suffix");
        assertEquals(List.of("a@b.com"), emails);
    }

    // ==================================================
    // extractPhones
    // ==================================================

    @Test
    void extractPhones_shouldThrowOnNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.extractPhones(null));
    }

    @Test
    void extractPhones_shouldReturnEmptyListForEmptyString() {
        assertTrue(service.extractPhones("").isEmpty());
    }

    @Test
    void extractPhones_shouldReturnEmptyListWhenNoPhones() {
        assertTrue(service.extractPhones("Hello, world!").isEmpty());
    }

    @Test
    void extractPhones_shouldFindPlus375Compact() {
        List<String> phones = service.extractPhones("call +375291234567");
        assertEquals(List.of("+375291234567"), phones);
    }

    @Test
    void extractPhones_shouldFind80Compact() {
        List<String> phones = service.extractPhones("call 80291234567");
        assertEquals(List.of("80291234567"), phones);
    }

    @Test
    void extractPhones_shouldFindPlus375Formatted() {
        List<String> phones = service.extractPhones("call +375 (29) 123-45-67");
        assertEquals(List.of("+375 (29) 123-45-67"), phones);
    }

    @Test
    void extractPhones_shouldFind80Formatted() {
        List<String> phones = service.extractPhones("call 80 (29) 123-45-67");
        assertEquals(List.of("80 (29) 123-45-67"), phones);
    }

    @Test
    void extractPhones_shouldNotMatchLongNumber() {
        assertTrue(service.extractPhones("order 123456789012345").isEmpty());
    }

    @Test
    void extractPhones_shouldNotMatchPrefixInsideLongNumber() {
        // 37529123456789 — не телефон, слишком много цифр
        assertTrue(service.extractPhones("37529123456789").isEmpty());
    }

    @Test
    void extractPhones_shouldReturnUniquePhones() {
        List<String> phones = service.extractPhones("+375291234567 and +375291234567");
        assertEquals(List.of("+375291234567"), phones);
    }

    // ==================================================
    // removeEmails
    // ==================================================

    @Test
    void removeEmails_shouldThrowOnNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.removeEmails(null));
    }

    @Test
    void removeEmails_shouldReturnSameTextForEmptyString() {
        assertEquals("", service.removeEmails(""));
    }

    @Test
    void removeEmails_shouldRemoveSingleEmail() {
        String result = service.removeEmails("Contact me at john.doe@example.com");
        assertFalse(result.contains("john.doe@example.com"));
        assertTrue(result.contains("Contact me at"));
    }

    @Test
    void removeEmails_shouldRemoveAllEmails() {
        String result = service.removeEmails("a@b.com and c@d.org");
        assertFalse(result.contains("a@b.com"));
        assertFalse(result.contains("c@d.org"));
    }

    @Test
    void removeEmails_shouldKeepTextWithoutEmails() {
        String text = "Hello, world!";
        assertEquals(text, service.removeEmails(text));
    }

    // ==================================================
    // removePhones
    // ==================================================

    @Test
    void removePhones_shouldThrowOnNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.removePhones(null));
    }

    @Test
    void removePhones_shouldReturnSameTextForEmptyString() {
        assertEquals("", service.removePhones(""));
    }

    @Test
    void removePhones_shouldRemovePlus375Compact() {
        String result = service.removePhones("call +375291234567 now");
        assertFalse(result.contains("+375291234567"));
        assertTrue(result.contains("call"));
        assertTrue(result.contains("now"));
    }

    @Test
    void removePhones_shouldRemoveFormattedPhone() {
        String result = service.removePhones("call +375 (29) 123-45-67 now");
        assertFalse(result.contains("+375 (29) 123-45-67"));
    }

    @Test
    void removePhones_shouldNotTouchLongNumber() {
        String text = "order 123456789012345";
        assertEquals(text, service.removePhones(text));
    }

    @Test
    void removePhones_shouldKeepTextWithoutPhones() {
        String text = "Hello, world!";
        assertEquals(text, service.removePhones(text));
    }

    // ==================================================
    // removeAll
    // ==================================================

    @Test
    void removeAll_shouldThrowOnNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.removeAll(null));
    }

    @Test
    void removeAll_shouldReturnSameTextForEmptyString() {
        assertEquals("", service.removeAll(""));
    }

    @Test
    void removeAll_shouldRemoveEmailsAndPhones() {
        String result = service.removeAll("a@b.com and +375291234567");
        assertFalse(result.contains("a@b.com"));
        assertFalse(result.contains("+375291234567"));
    }

    @Test
    void removeAll_shouldPreserveOtherText() {
        String result = service.removeAll("Hello a@b.com and +375291234567 world");
        assertTrue(result.contains("Hello"));
        assertTrue(result.contains("world"));
    }

    @Test
    void removeAll_shouldNotTouchLongNumber() {
        String result = service.removeAll("order 123456789012345");
        assertTrue(result.contains("123456789012345"));
    }

    // ==================================================
    // Комплексный сценарий
    // ==================================================

    @Test
    void extractAndRemove_roundTrip() {
        String text = "Contact me at john.doe@example.com or +375 (29) 123-45-67.";
        List<String> emails = service.extractEmails(text);
        List<String> phones = service.extractPhones(text);
        String cleaned = service.removeAll(text);

        assertEquals(List.of("john.doe@example.com"), emails);
        assertEquals(List.of("+375 (29) 123-45-67"), phones);
        assertFalse(cleaned.contains("john.doe@example.com"));
        assertFalse(cleaned.contains("+375 (29) 123-45-67"));
        assertTrue(cleaned.contains("Contact me at"));
    }
}
