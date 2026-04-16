package com.herani.ussd_demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    @Value("${auth.api.url}")
    private String url;
    private static final String LOGIN_URL = "https://dummyjson.com/auth/login";
        RestTemplate restTemplate = new RestTemplate();
    public boolean authenticateUserOnlyPin(String pin) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("device_uuid", "924224a1ae5b3efc");
        headers.set("platform", "android");
        headers.set("installation_date", "2026-03-04T12:38:01.627Z");
        headers.set("x-source", "app");
        headers.set("app_version", "1.0");

        Map<String, String> body = Map.of("pin", pin);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return response.getStatusCode().is2xxSuccessful();
    }
    public boolean authenticateUserFromDummyJson(String pin){
        String username = getUsernameFromPin(pin);
        Map<String, String> body = Map.of("username", username, "password", pin);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(LOGIN_URL, request, String.class);
        return response.getStatusCode().is2xxSuccessful();
    }

    private String getUsernameFromPin(String pin) {
        switch (pin) {
            case "1234": return "kminchelle";   // Known working user in DummyJSON
            case "0000": return "atuny0";       // Another working user
            case "1111": return "hbingley1";
            default:     return "invaliduser";  // Will fail
        }
    }

    public boolean authenticateUser(String pin) {
        // Basic validation for USSD PIN (must be exactly 4 digits)
        if (pin == null || pin.length() != 4 || !pin.matches("\\d{4}")) {
            return false;
        }
            // Use a currently working credential from DummyJSON
            String username = "emilys";           // Reliable working username
            String password = "emilyspass";       // Correct password for emilys

            // For demo purposes: allow multiple easy PINs
            // You can add more mappings if you want
            switch (pin) {
                case "1234":
                    password = "emilyspass";   // Success
                    break;
                case "0000":
                case "1111":
                case "9999":
                    return true;               // Force success for quick testing
                default:
                    return false;              // Any other PIN fails
            }

            Map<String, String> body = new HashMap<>();
            body.put("username", username);
            body.put("password", password);
            // body.put("expiresInMins", "30"); // optional

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(LOGIN_URL, request, String.class);

            return response.getStatusCode().is2xxSuccessful();
    }
}
