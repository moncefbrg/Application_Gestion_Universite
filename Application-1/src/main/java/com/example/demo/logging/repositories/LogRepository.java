package com.example.demo.logging.repositories;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.logging.entities.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

	void deleteByLogDateBefore(LocalDateTime thresholdDate);
}
