package controller;

import org.example.controller.ObjetController;
import org.example.model.Objet;
import org.example.service.ObjetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ObjetControllerTest {

    @Mock
    private ObjetService objetService;

    @InjectMocks
    private ObjetController objetController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(objetController).build();
    }

    // Test pour ajouter un objet
    @Test
    public void testAjouterObjet() throws Exception {
        Objet objet = new Objet();
        objet.setId(1);
        objet.setNom("Objet Test");
        objet.setDescription("Description de l'objet test");

        // Utilisation de doReturn pour contourner le strict stubbing
        doReturn("Objet ajouté avec succès").when(objetService).ajouterObjet(org.mockito.ArgumentMatchers.any(Objet.class));

        mockMvc.perform(post("/objets/ajouter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"nom\": \"Objet Test\", \"description\": \"Description de l'objet test\" }"))
                .andExpect(status().isOk())
                .andExpect(content().string("Objet ajouté avec succès"));
    }

    // Test pour rechercher des objets par mot-clé
    @Test
    public void testRechercherObjets() throws Exception {
        Objet objet1 = new Objet();
        objet1.setId(1);
        objet1.setNom("Objet 1");
        objet1.setDescription("Description 1");

        // Simule la réponse du service
        doReturn(Collections.singletonList(objet1)).when(objetService).rechercherObjets("Objet");

        mockMvc.perform(get("/objets/rechercher")
                        .param("keyword", "Objet"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"id\":1,\"nom\":\"Objet 1\",\"description\":\"Description 1\"}]"));  // Vérifie la réponse JSON
    }

    // Test pour récupérer tous les objets d'un utilisateur
    @Test
    public void testGetObjetsByUtilisateurId() throws Exception {
        Objet objet1 = new Objet();
        objet1.setId(1);
        objet1.setNom("Objet 1");
        objet1.setDescription("Description 1");

        // Stubbing pour la méthode
        doReturn(Collections.singletonList(objet1)).when(objetService).getObjetsByUtilisateurId(1);

        mockMvc.perform(get("/objets/utilisateur/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"id\":1,\"nom\":\"Objet 1\",\"description\":\"Description 1\"}]"));  // Vérifie la réponse JSON
    }
}
