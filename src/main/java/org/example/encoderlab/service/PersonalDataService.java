package org.example.encoderlab.service;

import org.example.encoderlab.cache.PersonalDataCache;
import org.example.encoderlab.counter.RequestCounter;
import org.example.encoderlab.dto.PersonalDataResponse;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PersonalDataService {

    private static final Pattern EMAIL =
            Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");

    private static final Pattern PHONE =
            Pattern.compile("(?<!\\d)(\\+375|80)([\\s\\-]?\\(?\\d{2}\\)?[\\s\\-]?\\d{3}[\\s\\-]?\\d{2}[\\s\\-]?\\d{2})(?!\\d)");

    private final PersonalDataCache cache;
    private final RequestCounter counter;

    public PersonalDataService(PersonalDataCache cache, RequestCounter counter) {
        this.cache = cache;
        this.counter = counter;
    }

    public PersonalDataResponse process(String text, String mode) {
        counter.increment();
        String key = mode + ":" + text;
        PersonalDataResponse cached = cache.get(key);
        if (cached != null) {
            return cached;
        }
        PersonalDataResponse result = compute(text, mode);
        cache.put(key, result);
        return result;
    }

    public List<PersonalDataResponse> processBulk(List<String> texts, String mode) {
        return texts.stream()
                .map(t -> process(t, mode))
                .toList();
    }

    private PersonalDataResponse compute(String text, String mode) {
        return switch (mode) {
            case "extract" -> new PersonalDataResponse(
                    extractEmails(text),
                    extractPhones(text),
                    ""
            );
            case "remove" -> new PersonalDataResponse(
                    List.of(),
                    List.of(),
                    removeAll(text)
            );
            default -> throw new IllegalArgumentException("Unknown mode: " + mode);
        };
    }

    public List<String> extractEmails(String text) {
        checkNotNull(text);
        Matcher m = EMAIL.matcher(text);
        Set<String> result = new LinkedHashSet<>();
        while (m.find()) {
            result.add(m.group());
        }
        return List.copyOf(result);
    }

    public List<String> extractPhones(String text) {
        checkNotNull(text);
        Matcher m = PHONE.matcher(text);
        Set<String> result = new LinkedHashSet<>();
        while (m.find()) {
            result.add(m.group());
        }
        return List.copyOf(result);
    }

    public String removeEmails(String text) {
        checkNotNull(text);
        return EMAIL.matcher(text).replaceAll("");
    }

    public String removePhones(String text) {
        checkNotNull(text);
        return PHONE.matcher(text).replaceAll("");
    }

    public String removeAll(String text) {
        checkNotNull(text);
        return removePhones(removeEmails(text));
    }

    private void checkNotNull(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Argument should not be NULL");
        }
    }

}