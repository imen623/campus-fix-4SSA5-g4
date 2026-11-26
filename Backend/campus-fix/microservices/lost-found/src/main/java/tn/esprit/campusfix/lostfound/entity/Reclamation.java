package tn.esprit.campusfix.lostfound.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reclamations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"declaration_id", "userId"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reclamation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "declaration_id", nullable = false)
    private Declaration declaration;

    // ID from User microservice - no foreign key
    @Column(nullable = false)
    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutReclamation statut;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dateReclamation;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
