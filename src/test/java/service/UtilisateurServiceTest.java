package service;

import org.example.model.Utilisateur;
import org.example.repository.UtilisateurRepository;
import org.example.service.UtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UtilisateurServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private UtilisateurService utilisateurService;

    private Utilisateur utilisateur;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Préparation d'un utilisateur pour les tests
        utilisateur = new Utilisateur();
        utilisateur.setId(1);
        utilisateur.setNom("John Doe");
        utilisateur.setEmail("john.doe@example.com");
        utilisateur.setPassword("password");
    }

    // Test de la méthode 'inscrireUtilisateur'
    @Test
    public void testInscrireUtilisateur() {
        // Simuler le comportement de la méthode save de utilisateurRepository
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);

        // Appeler la méthode d'inscription
        Utilisateur result = utilisateurService.inscrireUtilisateur("John Doe", "john.doe@example.com", "password");

        // Vérifier les assertions
        assertNotNull(result);
        assertEquals("John Doe", result.getNom());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("password", result.getPassword());
        verify(utilisateurRepository, times(1)).save(any(Utilisateur.class));
    }

    // Test de la méthode 'seConnecter' avec succès
    @Test
    public void testSeConnecterSuccess() {
        // Simuler la recherche de l'utilisateur par email
        when(utilisateurRepository.findByEmail("john.doe@example.com")).thenReturn(utilisateur);

        // Appeler la méthode de connexion
        ResponseEntity<Map<String, Object>> response = utilisateurService.seConnecter("john.doe@example.com", "password");

        // Vérifier les assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.get("id"));
        assertEquals("John Doe", body.get("nom"));
        assertEquals("john.doe@example.com", body.get("email"));
        verify(utilisateurRepository, times(1)).findByEmail("john.doe@example.com");
    }

    // Test de la méthode 'seConnecter' avec mot de passe incorrect
    @Test
    public void testSeConnecterMotDePasseIncorrect() {
        // Simuler la recherche de l'utilisateur par email
        when(utilisateurRepository.findByEmail("john.doe@example.com")).thenReturn(utilisateur);

        // Vérifier que la ResponseStatusException est lancée pour un mot de passe incorrect
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            utilisateurService.seConnecter("john.doe@example.com", "wrongpassword");
        });

        // Correction de la vérification : utiliser getStatusCode() pour récupérer le statut
        assertEquals(401, exception.getStatusCode().value());  // Vérification du statut HTTP
        assertEquals("Mot de passe incorrect", exception.getReason());  // Vérification du message d'erreur
    }

    // Test de la méthode 'seConnecter' avec utilisateur non trouvé
    @Test
    public void testSeConnecterUtilisateurNonTrouve() {
        // Simuler qu'aucun utilisateur n'est trouvé avec cet email
        when(utilisateurRepository.findByEmail("non.existant@example.com")).thenReturn(null);

        // Vérifier que la ResponseStatusException est lancée pour un utilisateur non trouvé
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            utilisateurService.seConnecter("non.existant@example.com", "password");
        });

        // Correction de la vérification : utiliser getStatusCode() pour récupérer le statut
        assertEquals(401, exception.getStatusCode().value());  // Vérification du statut HTTP
        assertEquals("Utilisateur non trouvé", exception.getReason());  // Vérification du message d'erreur
    }
}
