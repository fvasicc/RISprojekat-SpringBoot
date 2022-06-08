package com.example.aerodrom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import model.AaerodromAccount;
import model.Aputnik;

public interface PutnikRepository extends JpaRepository<Aputnik, Integer> {

	Aputnik findByAaerodromAccount(AaerodromAccount aaerodromAccount);
	
	Aputnik findByEmail(String email);
}
