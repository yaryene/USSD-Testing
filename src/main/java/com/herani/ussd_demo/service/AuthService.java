package com.herani.ussd_demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthService {
    @Value("${auth.api.url}")
    private String url;
    public boolean authenticateUser(String pin) {
        RestTemplate restTemplate = new RestTemplate();
//        String url = "https://qaapicbesuperapp.eaglelionsystems.com/api/v1/cbesuperapp/member_auth/login";
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
}
