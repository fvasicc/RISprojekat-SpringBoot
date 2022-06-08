package com.example.aerodrom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Astatusleta;

public interface StatusLetaRepository extends JpaRepository<Astatusleta, Integer> {
	
	Astatusleta findByStatus(String status);

}
