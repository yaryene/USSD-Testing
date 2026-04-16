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
@RestController
@RequestMapping("/ussd")
public class UssdController {

    private final AuthService authService;

    public UssdController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public String handleUssd(@RequestParam String sessionId,
                             @RequestParam String phoneNumber,
                             @RequestParam(required = false) String text) {

        String userText = (text == null || text.trim().isEmpty()) ? "" : text.trim();

        System.out.println("=== USSD DEBUG === Text: '" + userText + "' | Parts: " + userText.split("\\*").length);

        // Step 1: First request - Welcome & ask PIN
        if (userText.isEmpty()) {
            return "CON Welcome to SB Bank IBMB.\nPlease enter your 4-digit PIN:";
        }

        String[] inputs = userText.split("\\*");

        // Step 2: Only PIN (exactly 1 part) → This is the login step
        if (inputs.length == 1) {
            String pin = inputs[0].trim();

            if (pin.length() != 4 || !pin.matches("\\d{4}")) {
                return "END Invalid PIN. Please enter a 4-digit PIN.";
            }

            boolean isValid = authService.authenticateUser(pin);
            if (isValid) {
                return "CON Main Menu\n1. My Account\n2. Own Account Transfer\n3. Exit";
            } else {
                return "END Invalid PIN. Please try again.";
            }
        }

        // Step 3: After successful login (at least 2 parts: PIN*choice...)
        if (inputs.length >= 2) {
            String mainChoice = inputs[1].trim();

            // 1. My Account
            if ("1".equals(mainChoice)) {
                if (inputs.length == 2) {
                    // Show sub-menu
                    return "CON My Account\n1. View Balance\n2. Mini Statement\n3. Back to Main Menu";
                }
                if (inputs.length == 3) {
                    String subChoice = inputs[2].trim();
                    if ("1".equals(subChoice)) {
                        return "END Account: 1000123456789\n" +
                                "Name: Abebe Kebede\n" +
                                "Balance: ETB 24,850.75\n" +
                                "Available: ETB 24,850.75";
                    }
                    if ("2".equals(subChoice)) {
                        return "END Mini Statement:\n• 12 Apr - Sent 500 ETB\n• 10 Apr + Received 2,000 ETB\n• 08 Apr - Sent 1,200 ETB\n\nCurrent Balance: ETB 24,850.75";
                    }
                    if ("3".equals(subChoice)) {
                        return "CON Main Menu\n1. My Account\n2. Own Account Transfer\n3. Exit";
                    }
                }
                return "END Invalid sub-option in My Account.";
            }

            // 2. Own Account Transfer (placeholder)
            if ("2".equals(mainChoice)) {
                return "END Own Account Transfer feature coming soon!";
            }

            // 3. Exit
            if ("3".equals(mainChoice)) {
                return "END Thank you for using Shabelle Bank IBMB.\nGoodbye!";
            }
        }

        return "END Invalid option. Please start again by dialing *123#";
    }
}