package com.example.aerodrom.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.AaerodromAccount;

@Repository
@Transactional
public interface AerodromKorisnikRepository extends JpaRepository<AaerodromAccount, Integer> {

	AaerodromAccount findByUsername(String username);
	
}
