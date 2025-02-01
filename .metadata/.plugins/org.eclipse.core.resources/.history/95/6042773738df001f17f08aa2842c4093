package com.example.demo.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Module {
    @Id
    private Long id;

    @Column(unique = true)
    private String nom;
    private String semestre;
    
    @ManyToOne
    @JsonManagedReference
    private Niveau niveau;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Element> elements;

    @ManyToMany(mappedBy = "modules",fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Etudiant> etudiants;

    @ManyToOne
    @JoinColumn(name = "enseignant_id")
    @JsonManagedReference

    private Enseignant enseignant;
    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", nom='" + nom  +
                ",semestre ="+semestre+
                                '}';
    }
}
