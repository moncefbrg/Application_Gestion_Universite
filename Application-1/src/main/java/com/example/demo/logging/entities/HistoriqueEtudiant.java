package com.example.demo.logging.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Historique_Etudiant")
public class HistoriqueEtudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "logDate")
    private LocalDateTime logDate;
   

    @Column(name = "old_Values")
    private String oldValues;
    @Column(name = "new_Values")
    private String newValues;

    }