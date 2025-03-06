package controller;

import org.example.controller.EchangeController;
import org.example.model.Echange;
import org.example.model.Objet;
import org.example.model.Utilisateur;
import org.example.service.EchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EchangeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EchangeService echangeService;

    @InjectMocks
    private EchangeController echangeController;

    private Echange echange;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(echangeController).build();

        // Initialisation d'un objet Echange pour les tests
        echange = new Echange();
        echange.setId(1);
        echange.setStatut("EN_ATTENTE");
        echange.setDateDemande(LocalDateTime.now());

        // Création des objets nécessaires pour les tests
        Objet objetOffert = new Objet();
        objetOffert.setId(1);
        echange.setObjetOffert(objetOffert);

        Objet objetDemande = new Objet();
        objetDemande.setId(2);
        echange.setObjetDemande(objetDemande);

        Utilisateur demandeur = new Utilisateur();
        demandeur.setId(1);
        echange.setDemandeur(demandeur);

        Utilisateur receveur = new Utilisateur();
        receveur.setId(2);
        echange.setReceveur(receveur);
    }

    @Test
    public void testProposerEchange() {
        // Créer un objet Echange simulé avec un statut
        Echange mockEchange = new Echange();
        mockEchange.setStatut("EN_ATTENTE");

        // Définir le comportement du service lorsque la méthode est appelée
        when(echangeService.proposerEchange(1, 2, 3, 4)).thenReturn(mockEchange);

        // Appeler la méthode directement du contrôleur
        Echange result = echangeController.proposerEchange(new Echange() {{
            setObjetOffert(new Objet() {{ setId(1); }});
            setObjetDemande(new Objet() {{ setId(2); }});
            setDemandeur(new Utilisateur() {{ setId(3); }});
            setReceveur(new Utilisateur() {{ setId(4); }});
        }});

        // Vérifier que le résultat est correct
        assertEquals("EN_ATTENTE", result.getStatut());  // Vérifie que le statut retourné est bien "EN_ATTENTE"
    }

    @Test
    public void testAccepterEchange() throws Exception {
        // Simuler la réponse de la méthode accepterEchange du service
        when(echangeService.accepterEchange(anyInt())).thenReturn(echange);

        // Tester l'appel HTTP POST à /echanges/accepter/{id}
        mockMvc.perform(MockMvcRequestBuilders.post("/echanges/accepter/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("EN_ATTENTE")) // L'état ne change pas dans cet exemple
                .andExpect(jsonPath("$.id").value(1));

        // Vérifier que la méthode du service a été appelée une fois
        verify(echangeService, times(1)).accepterEchange(1);
    }

    @Test
    public void testRefuserEchange() throws Exception {
        // Simuler la réponse de la méthode refuserEchange du service
        when(echangeService.refuserEchange(anyInt())).thenReturn(echange);

        // Tester l'appel HTTP POST à /echanges/refuser/{id}
        mockMvc.perform(MockMvcRequestBuilders.post("/echanges/refuser/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("EN_ATTENTE")) // Idem pour la refuser
                .andExpect(jsonPath("$.id").value(1));

        // Vérifier que la méthode du service a été appelée une fois
        verify(echangeService, times(1)).refuserEchange(1);
    }

    @Test
    public void testGetEchangesByUtilisateurId() throws Exception {
        // Simuler la réponse de la méthode getEchangesByUtilisateurId du service
        when(echangeService.getEchangesByUtilisateurId(anyInt())).thenReturn(List.of(echange));

        // Tester l'appel HTTP GET à /echanges/utilisateur/{utilisateurId}
        mockMvc.perform(MockMvcRequestBuilders.get("/echanges/utilisateur/{utilisateurId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].statut").value("EN_ATTENTE"))
                .andExpect(jsonPath("$[0].id").value(1));

        // Vérifier que la méthode du service a été appelée une fois
        verify(echangeService, times(1)).getEchangesByUtilisateurId(1);
    }
}
