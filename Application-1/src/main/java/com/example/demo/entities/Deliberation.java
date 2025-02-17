package com.example.demo.entities;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Deliberation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_deliberation")
    private Date dateDeliberation;

    @OneToMany(mappedBy = "deliberation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResultatElement> resultatElement;

    @Column(name = "note_finale")
    private Double noteFinale;
}