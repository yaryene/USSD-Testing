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
            return "CON Welcome to SB Bank IBMB.\nPlease enter your PIN:";
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
}
