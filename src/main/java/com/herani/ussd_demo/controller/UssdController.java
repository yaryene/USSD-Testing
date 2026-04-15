package com.herani.ussd_demo.controller;

import com.herani.ussd_demo.service.AuthService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@RestController
@RequestMapping("/ussd")
public class UssdController {
    private final AuthService authService;

    public UssdController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public String handleUssd(@RequestParam String sessionId, @RequestParam String phoneNumber, @RequestParam String text) {

        String[] inputs = text.split("\\*");

        // step 1: First Dial
        if(text.isEmpty()){
            return "CON Welcome to Shabelle Bank IBMB.\nPlease enter your PIN:";
        }

        //  step 2: PIN Entry
        if(inputs.length == 1){
            String pin = inputs[0];

            boolean isValid = authService.authenticateUser(pin);
            if(isValid){
                return "CON 1. My Account\n2. Own Account Transfer";
            }
            else {
                return "END Invalid PIN. Please enter a 4-digit PIN:";
            }
        }
        // step 3: Menu Selection
        if(inputs.length == 2){
            String option = inputs[1];
            switch (option){
                case "1":
                    return "END Your balance is 100 ETB";
                case "2":
                    return "END Transfer feature coming soon!";
                default:
                    return "END Invalid option.";
            }
        }
        return "END Error";
    }
//369258
//    0932948548
    // call your real api
    // RestTemplate (simple for now)
//    private boolean authenticateUser(String pin) {
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "https://qaapicbesuperapp.eaglelionsystems.com/api/v1/cbesuperapp/member_auth/login";
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("device_uuid", "924224a1ae5b3efc");
//        headers.set("platform", "android");
//        headers.set("installation_date", "2026-03-04T12:38:01.627Z");
//        headers.set("x-source", "app");
//        headers.set("app_version", "1.0");
//
//        Map<String, String> body = Map.of("pin", pin);
//
//        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
//        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
//        return response.getStatusCode().is2xxSuccessful();
//        // Add any required headers here
//        // headers.set("Authorization", "Bearer " + token);
//    }
}
