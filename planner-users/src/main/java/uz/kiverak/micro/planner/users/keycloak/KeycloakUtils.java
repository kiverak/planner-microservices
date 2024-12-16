package uz.kiverak.micro.planner.users.keycloak;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.kiverak.micro.planner.users.dto.UserDto;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response;
import java.util.Collections;

@Service
public class KeycloakUtils {

    @Value("${keycloak.auth-server-url}")
    private String serverURL;
    @Value("${keycloak.resource-realm}")
    private String realm;
    @Value("${keycloak.resource-user}")
    private String clientID;
    @Value("${keycloak.credentials.secret}")
    private String secret;

    private static Keycloak keyCloak;
    private static RealmResource realmResource; // доступ к API realm
    private static UsersResource usersResource; // доступ к API работы с пользователем

    @PostConstruct
    public Keycloak initKeycloak() {
        if (keyCloak == null) {
            keyCloak = KeycloakBuilder.builder()
                    .realm(realm)
                    .serverUrl(serverURL)
                    .clientId(clientID)
                    .clientSecret(secret)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .build();

            realmResource = keyCloak.realm(realm);
            usersResource = realmResource.users();
        }

        return keyCloak;
    }

    public Response createKeycloakUser(UserDto userDto) {

        CredentialRepresentation credentialRepresentation = createPasswordCredentials(userDto.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(userDto.getUsername());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setEmail(userDto.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        return usersResource.create(kcUser);
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        return credential;
    }
}
