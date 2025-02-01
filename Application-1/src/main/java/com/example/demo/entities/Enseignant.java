package com.example.demo.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enseignant {
    @Id
    private Long id;
    
    private String nom;
    private String prenom;
    
    @Column(unique = true, nullable=false)
    private String cni;
    
    @OneToMany(mappedBy = "enseignant", cascade = CascadeType.ALL, orphanRemoval = true,fetch=FetchType.EAGER)
    @ToString.Exclude
    private List<Module> modules;
}

