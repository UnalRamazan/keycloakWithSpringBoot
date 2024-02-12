package com.example.mybackend.Controller;

import com.example.mybackend.Service.FileService;
import com.example.mybackend.Service.KeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

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

    @GetMapping("/getAllClientSecret/{realmName}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> getAllClientSecrets(
            @PathVariable String realmName
    ) {
        try {
            List<String> clientSecrets = keycloakService.getAllClientSecrets(realmName);

            if (clientSecrets.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No client secrets were found for the specified region.");
            }

            FileService.writeToFile(clientSecrets, realmName);

            System.out.println("Client secrets have been successfully retrieved and written to the" + realmName + ".txt file.");
            return ResponseEntity.ok(toString(clientSecrets));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while writing client secrets to the file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving all client secrets: " + e.getMessage());
        }
    }

    private String toString(List<String> stringList){
        StringBuilder temp = new StringBuilder();
        for(String str: stringList){

            temp.append(str).append("\n");
        }

        return temp.toString();
    }
}
