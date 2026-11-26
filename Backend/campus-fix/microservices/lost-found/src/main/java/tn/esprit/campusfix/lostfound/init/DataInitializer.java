package tn.esprit.campusfix.lostfound.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tn.esprit.campusfix.lostfound.entity.*;
import tn.esprit.campusfix.lostfound.repository.DeclarationRepository;
import tn.esprit.campusfix.lostfound.repository.ReclamationRepository;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final DeclarationRepository declarationRepository;
    private final ReclamationRepository reclamationRepository;

    public DataInitializer(DeclarationRepository declarationRepository,
                           ReclamationRepository reclamationRepository) {
        this.declarationRepository = declarationRepository;
        this.reclamationRepository = reclamationRepository;
    }

    @Override
    public void run(String... args) {
        if (declarationRepository.count() > 0) {
            log.info("Des données existent déjà en base. Initialisation ignorée.");
            return;
        }

        log.info("Initialisation des données de test pour Lost & Found...");

        // 1. Objets perdus
        Declaration d1 = Declaration.builder()
                .titre("MacBook Pro 13\" noir")
                .description("Ordinateur portable Apple MacBook Pro 13 pouces, couleur gris sidéral. "
                        + "Autocollant CampusFix sur la coque. Perdu en salle TP3.")
                .categorie(CategorieObjet.ELECTRONIQUE)
                .typeDeclaration(TypeDeclaration.PERDU)
                .statut(StatutDeclaration.EN_ATTENTE)
                .userId(1L)
                .salleId(3L)
                .lieu("Salle TP3 - Bloc Informatique")
                .dateEvenement(LocalDate.now().minusDays(2))
                .build();

        Declaration d2 = Declaration.builder()
                .titre("Carte étudiant au nom de Aymen Trabelsi")
                .description("Carte d'étudiant ESPRIT valide jusqu'en 2025. "
                        + "Trouvée devant l'entrée principale du campus.")
                .categorie(CategorieObjet.DOCUMENT)
                .typeDeclaration(TypeDeclaration.TROUVE)
                .statut(StatutDeclaration.EN_ATTENTE)
                .userId(2L)
                .eventId(10L)
                .lieu("Entrée principale du campus")
                .dateEvenement(LocalDate.now().minusDays(1))
                .build();

        Declaration d3 = Declaration.builder()
                .titre("Clés de voiture Volkswagen")
                .description("Trousseau de 3 clés avec porte-clé rouge. Trouvé à la cafétéria du campus.")
                .categorie(CategorieObjet.CLE)
                .typeDeclaration(TypeDeclaration.TROUVE)
                .statut(StatutDeclaration.EN_ATTENTE)
                .userId(3L)
                .lieu("Cafétéria - RDC")
                .dateEvenement(LocalDate.now())
                .build();

        Declaration d4 = Declaration.builder()
                .titre("Veste en jean bleue taille M")
                .description("Veste en jean bleue, marque Levi's, taille M. Oubliée en salle de cours B201.")
                .categorie(CategorieObjet.VETEMENT)
                .typeDeclaration(TypeDeclaration.PERDU)
                .statut(StatutDeclaration.EN_ATTENTE)
                .userId(4L)
                .lieu("Salle B201 - Bloc B")
                .dateEvenement(LocalDate.now().minusDays(3))
                .build();

        declarationRepository.save(d1);
        declarationRepository.save(d2);
        declarationRepository.save(d3);
        declarationRepository.save(d4);

        // 2. Réclamations sur objets trouvés
        Reclamation r1 = Reclamation.builder()
                .declaration(d2)
                .userId(5L)
                .message("Je pense que c'est ma carte. Je peux fournir d'autres justificatifs d'identité.")
                .statut(StatutReclamation.EN_ATTENTE)
                .build();

        Reclamation r2 = Reclamation.builder()
                .declaration(d3)
                .userId(6L)
                .message("Ce sont mes clés de voiture. Ma voiture est une VW Golf grise garée au parking B.")
                .statut(StatutReclamation.EN_ATTENTE)
                .build();

        Reclamation r3 = Reclamation.builder()
                .declaration(d3)
                .userId(7L)
                .message("Ces clés ressemblent aux miennes. J'ai perdu mes clés hier matin.")
                .statut(StatutReclamation.EN_ATTENTE)
                .build();

        reclamationRepository.save(r1);
        reclamationRepository.save(r2);
        reclamationRepository.save(r3);

        log.info("Initialisation terminée : 4 déclarations (2 perdus, 2 trouvés) et 3 réclamations créées.");
    }
}
