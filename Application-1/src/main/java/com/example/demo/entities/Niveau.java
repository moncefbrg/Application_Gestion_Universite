package com.example.demo.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Niveau {
    @Id
    private Long id;

    private String nom;
    private String alias;

    @ManyToOne
    @JoinColumn(name = "niveau_suivant_id")
    private Niveau niveauSuivant;

    @OneToMany(mappedBy = "niveau", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Etudiant> etudiants;

    @OneToMany(mappedBy = "niveau", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Module> modules;

    @OneToMany(mappedBy = "niveau", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Formule> formules;

    // Relation avec le seuil
    @OneToOne
    @JoinColumn(name="sueil_id")
    private Seuil seuil;  // Relation inversée, "mappedBy" fait référence à l'attribut 'niveau' dans Seuil
    @Override
    public String toString() {
        return "Niveau{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", alias='" + alias + '\'' +
                // Ne pas inclure 'etudiants' ici pour éviter la récursivité infinie
                '}';
    }
}
