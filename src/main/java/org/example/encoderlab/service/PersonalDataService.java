package org.example.encoderlab.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PersonalDataService {
    private static final Pattern EMAIL = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
    private static final Pattern PHONE = Pattern.compile("(?<!\\d)(\\+375|80)([\\s\\-]?\\(?\\d{2}\\)?[\\s\\-]?\\d{3}[\\s\\-]?\\d{2}[\\s\\-]?\\d{2})(?!\\d)");

    public List<String> extractEmails(String text) {
        nonNullStringArgumentCheck(text, "Argument should not be NULL");
        Matcher m = EMAIL.matcher(text);
        List<String> result = new ArrayList<String>();
        while (m.find())
            result.add(m.group());
        return result;
    }

    public List<String> extractPhones(String text) {
        nonNullStringArgumentCheck(text, "Argument should not be NULL");
        Matcher m = PHONE.matcher(text);
        List<String> result = new ArrayList<String>();
        while (m.find())
            result.add(m.group());
        return result;
    }
    public String removeEmails(String text){
        nonNullStringArgumentCheck(text, "Argument should not be NULL");
        return EMAIL.matcher(text).replaceAll("");
    }

    public String removePhones(String text){
        nonNullStringArgumentCheck(text, "Argument should not be NULL");
        return PHONE.matcher(text).replaceAll("");
    }

    public String removeAll(String text) {
        return removePhones(removeEmails(text));
    }

    private void nonNullStringArgumentCheck(String arg, String msg){
        if(arg == null)
            throw new IllegalArgumentException(msg);
    }
}
