package com.example.demo.security.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.security.entities.Role;

public interface IRole extends JpaRepository<Role, Long>{
	Optional<Role> findByNom(String nom);

}
