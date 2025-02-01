package com.example.demo.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
@Entity
@Data @NoArgsConstructor @AllArgsConstructor  @Builder
public class Element {
	@Id
	private Long id;
	private String nom;
	
	@ManyToOne
	@JoinColumn(name="module_id", nullable=false)
	private Module module;
	
	@OneToMany(mappedBy="element", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<ResultatElement> ResultatsElements;
	@Override
    public String toString() {
        return "Element{" +
                "id=" + id +
                ", nom='" + nom  +
                                '}';
    }
		
}
