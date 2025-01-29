package com.example.demo.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity @Data @ToString @AllArgsConstructor @NoArgsConstructor @Builder
public class Deliberation {

	@Id
	private Long id;
	@Temporal(TemporalType.DATE)
    @Column(name = "date_deliberation")
    private Date dateDeliberation;
	@OneToOne
    @JoinColumn(name = "id_resultat_element", nullable = false)
    private ResultatElement resultatElement;

    @Column(name = "note_finale")
    private Double noteFinale;
	
}
