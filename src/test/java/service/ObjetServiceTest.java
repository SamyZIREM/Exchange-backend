package service;

import org.example.model.Objet;
import org.example.model.Utilisateur;
import org.example.repository.ObjetRepository;
import org.example.repository.UtilisateurRepository;
import org.example.service.ObjetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ObjetServiceTest {

    @Mock
    private ObjetRepository objetRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private ObjetService objetService;

    private Utilisateur utilisateur;
    private Objet objet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Préparation des objets pour les tests
        utilisateur = new Utilisateur();
        utilisateur.setId(1);
        utilisateur.setNom("John Doe");
        utilisateur.setEmail("john.doe@example.com");
        utilisateur.setPassword("password");

        objet = new Objet();
        objet.setId(1);
        objet.setNom("Objet de test");
        objet.setDescription("Description de l'objet");
        objet.setUtilisateur(utilisateur);
    }

    // Test de la méthode 'ajouterObjet'
    @Test
    public void testAjouterObjet() {
        // Simuler la récupération de l'utilisateur
        when(utilisateurRepository.findById(1)).thenReturn(Optional.of(utilisateur));

        // Simuler le comportement du save
        when(objetRepository.save(any(Objet.class))).thenReturn(objet);

        // Appeler la méthode
        String result = objetService.ajouterObjet(objet);

        // Vérifier les assertions
        assertEquals("Objet ajouté avec succès", result);
        verify(utilisateurRepository, times(1)).findById(1);
        verify(objetRepository, times(1)).save(objet);
    }

    @Test
    public void testAjouterObjetUtilisateurNonTrouve() {
        // Simuler l'absence de l'utilisateur
        when(utilisateurRepository.findById(1)).thenReturn(Optional.empty());

        // Vérifier si l'exception est bien levée
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            objetService.ajouterObjet(objet);
        });

        assertEquals("Utilisateur non trouvé", exception.getMessage());
    }

    // Test de la méthode 'rechercherObjets'
    @Test
    public void testRechercherObjets() {
        // Création de quelques objets pour le test
        Objet objet1 = new Objet();
        objet1.setNom("Objet de test");
        Objet objet2 = new Objet();
        objet2.setNom("Objet spécial");

        List<Objet> objets = Arrays.asList(objet1, objet2);

        // Simuler le comportement du repository
        when(objetRepository.findByNomContaining("test")).thenReturn(objets);

        // Appeler la méthode
        List<Objet> result = objetService.rechercherObjets("test");

        // Vérifier les assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(o -> o.getNom().contains("test")));
        verify(objetRepository, times(1)).findByNomContaining("test");
    }

    @Test
    public void testRechercherObjetsAucunResultat() {
        // Simuler un résultat vide
        when(objetRepository.findByNomContaining("inexistant")).thenReturn(Collections.emptyList());

        // Appeler la méthode
        List<Objet> result = objetService.rechercherObjets("inexistant");

        // Vérifier les assertions
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(objetRepository, times(1)).findByNomContaining("inexistant");
    }

    // Test de la méthode 'getObjetsByUtilisateurId'
    @Test
    public void testGetObjetsByUtilisateurId() {
        // Création d'objets associés à un utilisateur
        Objet objet1 = new Objet();
        objet1.setNom("Objet 1");
        Objet objet2 = new Objet();
        objet2.setNom("Objet 2");

        List<Objet> objets = Arrays.asList(objet1, objet2);

        // Simuler la récupération de l'utilisateur et ses objets
        when(utilisateurRepository.findById(1)).thenReturn(Optional.of(utilisateur));
        when(objetRepository.findByUtilisateur(utilisateur)).thenReturn(objets);

        // Appeler la méthode
        List<Objet> result = objetService.getObjetsByUtilisateurId(1);

        // Vérifier les assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(utilisateurRepository, times(1)).findById(1);
        verify(objetRepository, times(1)).findByUtilisateur(utilisateur);
    }

    @Test
    public void testGetObjetsByUtilisateurIdUtilisateurNonTrouve() {
        // Simuler l'absence de l'utilisateur
        when(utilisateurRepository.findById(1)).thenReturn(Optional.empty());

        // Vérifier si l'exception est bien levée
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            objetService.getObjetsByUtilisateurId(1);
        });

        assertEquals("Utilisateur non trouvé", exception.getMessage());
    }
}
