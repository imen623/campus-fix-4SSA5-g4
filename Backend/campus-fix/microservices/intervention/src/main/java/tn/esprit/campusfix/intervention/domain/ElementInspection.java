package tn.esprit.campusfix.intervention.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "elements_inspection")
@Getter
@Setter
@NoArgsConstructor
public class ElementInspection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, length = 2000)
    private String valeur;

    @Column(nullable = false)
    private Boolean conformite;

    @Column(length = 2000)
    private String remarque;

    @ManyToOne(optional = false)
    @JoinColumn(name = "inspection_id", nullable = false)
    private Inspection inspection;

    @ManyToOne(optional = false)
    @JoinColumn(name = "element_id", nullable = false)
    private Element element;

    @ManyToOne(optional = false)
    @JoinColumn(name = "code_id", nullable = false)
    private Code code;


}
