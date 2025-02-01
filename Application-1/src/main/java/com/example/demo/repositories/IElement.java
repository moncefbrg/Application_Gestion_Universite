package com.example.demo.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Element;
import org.springframework.stereotype.Repository;
import com.example.demo.entities.Module;

@Repository
public interface IElement extends JpaRepository<Element, Long>{
	Optional<Element> findByNom(String nom);
	boolean existsByNom(String nom);
	List<Element> findByModule(Module module);

}
