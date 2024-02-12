package com.example.mybackend.Service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeycloakService {

    @Autowired
    private Keycloak keycloak;

    public String getClientSecret(String realmName, String clientName) {
        if (realmName == null || clientName == null) {
            return null;
        }

        //All Realm
        System.out.println("All Realm: ");
        RealmsResource realmsResource = keycloak.realms();
        for (RealmRepresentation realmRepresentation : realmsResource.findAll()) {
            System.out.println(realmRepresentation.getDisplayName());
        }
        System.out.println("-------------------------------------------------");

        //Selected Realm
        System.out.println("Selected Realm: " + realmName);
        RealmResource realmResource = keycloak.realm(realmName);
        System.out.println("-------------------------------------------------");

        //All Clients
        System.out.println("All Clients for " + realmName + " realm: ");
        ClientsResource clientsResource = realmResource.clients();
        for (ClientRepresentation clientRepresentation : clientsResource.findAll()) {
            System.out.println("Client: " + clientRepresentation.getClientId() + " --> Id: " + clientRepresentation.getId());
        }
        System.out.println("-------------------------------------------------");

        //Selected Client
        System.out.println("Selected Client: " + clientName);
        ClientResource clientResource = clientsResource.get(clientName);

        System.out.println("Secret: " + clientResource.getSecret().getValue());
        System.out.println("-------------------------------------------------");

        if (clientResource != null) {
            return clientResource.getSecret().getValue();
        }

        return null; // Belirtilen realm ve client bulunamadı
    }

    public List<String> getAllClientSecrets(String realmName) {
        List<String> clientSecrets = new ArrayList<>();

        if (realmName == null) {
            return clientSecrets;
        }

        // Selected Realm
        System.out.println("Selected Realm: " + realmName);
        RealmResource realmResource = keycloak.realm(realmName);
        System.out.println("-------------------------------------------------");

        // All Clients
        System.out.println("All Clients for " + realmName + " realm: ");
        ClientsResource clientsResource = realmResource.clients();
        for (ClientRepresentation clientRepresentation : clientsResource.findAll()) {
            String temp = "Client: " + clientRepresentation.getClientId() + " --> Id: " + clientRepresentation.getId();

            ClientResource clientResource = clientsResource.get(clientRepresentation.getId());

            temp += " ---> Secret: " + clientResource.getSecret().getValue();
            clientSecrets.add(temp);

            System.out.println(temp);
            System.out.println("-------------------------------------------------");
        }

        return clientSecrets;
    }
}
