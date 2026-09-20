package org.example.encoderlab.cache;

import org.example.encoderlab.dto.PersonalDataResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PersonalDataCache {

    private final Map<String, PersonalDataResponse> storage = new HashMap<>();

    public PersonalDataResponse get(String key) {
        return storage.get(key);
    }

    public void put(String key, PersonalDataResponse value) {
        storage.put(key, value);
    }
}