package tn.esprit.campusfix.lostfound.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "declarations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Declaration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategorieObjet categorie;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeDeclaration typeDeclaration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDeclaration statut;

    // ID from User microservice - no foreign key
    @Column(nullable = false)
    private Long userId;

    // Optional ID from Room microservice - no foreign key
    private Long salleId;

    // Optional ID from Event microservice - no foreign key
    private Long eventId;

    @Column(length = 200)
    private String lieu;

    private LocalDate dateEvenement;

    @Column(length = 500)
    private String imageUrl;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "declaration", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Reclamation> reclamations = new ArrayList<>();
}
