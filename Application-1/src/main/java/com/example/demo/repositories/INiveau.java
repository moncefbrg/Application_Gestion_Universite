package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Niveau;
@Repository
public interface INiveau extends JpaRepository<Niveau, Long>{

}
