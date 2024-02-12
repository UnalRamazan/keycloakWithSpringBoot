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

@Service
public class KeycloakService {

    @Autowired
    private Keycloak keycloak;

    public String getClientSecret(String realmName, String clientName) {
        if (realmName == null || clientName == null) {
            // Hata durumu: realmName veya clientName null ise
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
}
