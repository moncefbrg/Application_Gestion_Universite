
package com.example.demo.repositories;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Deliberation;

public interface IDeliberation extends JpaRepository<Deliberation, Long>{
	Optional<Deliberation> findByDateDeliberation(Date date);

}
