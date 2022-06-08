package com.example.aerodrom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import model.Alet;
import model.Aputnik;
import model.Arezervacija;

public interface RezervacijaRepository extends JpaRepository<Arezervacija, Integer> {
	
	List<Arezervacija> findByAputnik(Aputnik putnik);
	
	@Query("select distinct r from Arezervacija r inner join r.akartas k where k.alet = :let")
	List<Arezervacija> findByAlet(@Param("let") Alet let);

}
