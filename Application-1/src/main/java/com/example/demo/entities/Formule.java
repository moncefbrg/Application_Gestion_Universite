package com.example.demo.entities;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Formule {
    @Id
    private Long id;
    
    @Column(nullable=false)
    private String nom;
    
    @Column(nullable=false)
    private String expression;

    @ElementCollection
    @CollectionTable(name = "formule_parametres", joinColumns = @JoinColumn(name = "formule_id"))
    @Column(name = "parametre")
    private List<String> parametres;

    @ManyToOne
    private Niveau niveau;
}

	

