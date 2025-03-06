package controller;

import org.example.controller.UtilisateurController;
import org.example.model.Utilisateur;
import org.example.service.UtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UtilisateurControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UtilisateurService utilisateurService;

    @InjectMocks
    private UtilisateurController utilisateurController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(utilisateurController).build();
    }

    // ✅ Test de l'inscription
    @Test
    void testInscrireUtilisateur() throws Exception {
        // Création d'un utilisateur fictif
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1);
        utilisateur.setNom("John Doe");
        utilisateur.setEmail("johndoe@example.com");
        utilisateur.setPassword("password123");

        // Mock du service
        doReturn(utilisateur).when(utilisateurService).inscrireUtilisateur(
                any(String.class), any(String.class), any(String.class));

        // Exécution de la requête et vérification des résultats
        mockMvc.perform(post("/utilisateurs/inscription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"John Doe\",\"email\":\"johndoe@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("John Doe"))
                .andExpect(jsonPath("$.email").value("johndoe@example.com"));
    }

    // ✅ Test de la connexion
    @Test
    void testSeConnecter() throws Exception {
        // Simulation de la réponse attendue
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Connexion réussie");
        response.put("token", "fake-jwt-token");

        doReturn(ResponseEntity.ok(response)).when(utilisateurService).seConnecter(eq("johndoe@example.com"), eq("password123"));

        // Exécution de la requête et vérification des résultats
        mockMvc.perform(post("/utilisateurs/connexion")
                        .param("email", "johndoe@example.com")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Connexion réussie"))
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }
}
