package service;

import org.example.model.Echange;
import org.example.model.Objet;
import org.example.model.Utilisateur;
import org.example.repository.EchangeRepository;
import org.example.repository.ObjetRepository;
import org.example.repository.UtilisateurRepository;
import org.example.service.EchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EchangeServiceTest {

    @InjectMocks
    private EchangeService echangeService;

    @Mock
    private EchangeRepository echangeRepository;

    @Mock
    private ObjetRepository objetRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    private Objet objetOffert;
    private Objet objetDemande;
    private Utilisateur demandeur;
    private Utilisateur receveur;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Créer des objets mockés pour les tests
        objetOffert = new Objet();
        objetOffert.setId(1);
        objetOffert.setNom("Objet Offert");

        objetDemande = new Objet();
        objetDemande.setId(2);
        objetDemande.setNom("Objet Demandé");

        demandeur = new Utilisateur();
        demandeur.setId(1);
        demandeur.setNom("Demandeur");

        receveur = new Utilisateur();
        receveur.setId(2);
        receveur.setNom("Receveur");
    }

    @Test
    public void testProposerEchange() {
        // Préparer les comportements des mock repositories
        when(objetRepository.findById(1)).thenReturn(Optional.of(objetOffert));
        when(objetRepository.findById(2)).thenReturn(Optional.of(objetDemande));
        when(utilisateurRepository.findById(1)).thenReturn(Optional.of(demandeur));
        when(utilisateurRepository.findById(2)).thenReturn(Optional.of(receveur));

        // On simule le save, mais on renvoie l'objet avec statut "EN_ATTENTE"
        Echange echangeMock = new Echange();
        echangeMock.setStatut("EN_ATTENTE");
        when(echangeRepository.save(any(Echange.class))).thenReturn(echangeMock);

        // Appel de la méthode
        Echange echange = echangeService.proposerEchange(1, 2, 1, 2);

        // Vérifier les assertions
        assertNotNull(echange, "L'échange ne doit pas être null");
        assertEquals("EN_ATTENTE", echange.getStatut(), "Le statut de l'échange devrait être 'EN_ATTENTE'");

        // Optionnel : vérifier si les méthodes de repository ont bien été appelées
        verify(echangeRepository, times(1)).save(any(Echange.class));
        verify(objetRepository, times(1)).findById(1);
        verify(objetRepository, times(1)).findById(2);
        verify(utilisateurRepository, times(1)).findById(1);
        verify(utilisateurRepository, times(1)).findById(2);
    }


    @Test
    public void testProposerEchange_ObjetOffertNonTrouve() {
        // Préparer les comportements des mock repositories
        when(objetRepository.findById(1)).thenReturn(Optional.empty());
        when(objetRepository.findById(2)).thenReturn(Optional.of(objetDemande));

        // Appel de la méthode et vérification de l'exception
        assertThrows(RuntimeException.class, () -> echangeService.proposerEchange(1, 2, 1, 2));
    }

    @Test
    public void testAccepterEchange() {
        // Préparer les comportements des mock repositories
        Echange echange = new Echange();
        echange.setId(1);
        echange.setStatut("EN_ATTENTE");

        when(echangeRepository.findById(1)).thenReturn(Optional.of(echange));
        when(echangeRepository.save(any(Echange.class))).thenReturn(echange);

        // Appel de la méthode
        Echange echangeAccepté = echangeService.accepterEchange(1);

        // Vérifier les assertions
        assertNotNull(echangeAccepté);
        assertEquals("ACCEPTE", echangeAccepté.getStatut());
        verify(echangeRepository, times(1)).save(any(Echange.class));
    }

    @Test
    public void testAccepterEchange_EchangeNonTrouve() {
        // Préparer les comportements des mock repositories
        when(echangeRepository.findById(1)).thenReturn(Optional.empty());

        // Appel de la méthode et vérification de l'exception
        assertThrows(RuntimeException.class, () -> echangeService.accepterEchange(1));
    }

    @Test
    public void testRefuserEchange() {
        // Préparer les comportements des mock repositories
        Echange echange = new Echange();
        echange.setId(1);
        echange.setStatut("EN_ATTENTE");

        when(echangeRepository.findById(1)).thenReturn(Optional.of(echange));
        when(echangeRepository.save(any(Echange.class))).thenReturn(echange);

        // Appel de la méthode
        Echange echangeRefusé = echangeService.refuserEchange(1);

        // Vérifier les assertions
        assertNotNull(echangeRefusé);
        assertEquals("REFUSE", echangeRefusé.getStatut());
        verify(echangeRepository, times(1)).save(any(Echange.class));
    }

    @Test
    public void testRefuserEchange_EchangeNonTrouve() {
        // Préparer les comportements des mock repositories
        when(echangeRepository.findById(1)).thenReturn(Optional.empty());

        // Appel de la méthode et vérification de l'exception
        assertThrows(RuntimeException.class, () -> echangeService.refuserEchange(1));
    }

    @Test
    public void testGetEchangesByUtilisateurId() {
        // Préparer les comportements des mock repositories
        Echange echange1 = new Echange();
        echange1.setId(1);
        echange1.setStatut("EN_ATTENTE");

        Echange echange2 = new Echange();
        echange2.setId(2);
        echange2.setStatut("ACCEPTE");

        when(utilisateurRepository.findById(1)).thenReturn(Optional.of(demandeur));
        when(echangeRepository.findByDemandeurOrReceveur(demandeur, demandeur))
                .thenReturn(List.of(echange1, echange2));

        // Appel de la méthode
        List<Echange> echanges = echangeService.getEchangesByUtilisateurId(1);

        // Vérifier les assertions
        assertNotNull(echanges);
        assertEquals(2, echanges.size());
        verify(echangeRepository, times(1)).findByDemandeurOrReceveur(demandeur, demandeur);
    }

    @Test
    public void testGetEchangesByUtilisateurId_UtilisateurNonTrouve() {
        // Préparer les comportements des mock repositories
        when(utilisateurRepository.findById(1)).thenReturn(Optional.empty());

        // Appel de la méthode et vérification de l'exception
        assertThrows(RuntimeException.class, () -> echangeService.getEchangesByUtilisateurId(1));
    }
}
