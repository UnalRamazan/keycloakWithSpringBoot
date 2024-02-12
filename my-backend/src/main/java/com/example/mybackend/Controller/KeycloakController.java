package com.example.mybackend.Controller;

import com.example.mybackend.Service.KeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/keycloak")
public class KeycloakController {

    private final KeycloakService keycloakService;

    @Autowired
    public KeycloakController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @GetMapping("/getClientSecret/{realmName}/{clientName}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> getClientSecret(
            @PathVariable String realmName,
            @PathVariable String clientName
    ) {
        try {
            String clientSecret = keycloakService.getClientSecret(realmName, clientName);

            if (clientSecret != null) {
                return ResponseEntity.ok("Client Secret for " + clientName + " in realm " + realmName + ": " + clientSecret);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found in the specified realm.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while retrieving the client secret: " + e.getMessage());
        }
    }
}
