package com.example.aerodrom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import model.Alet;
import model.Amesto;

public interface MestoRepository extends JpaRepository<Amesto, Integer> {

	@Query("select distinct m from Amesto m inner join m.aavion.alets l where l = :let and m.aklasa.id = :idKlase "
			+ "and not exists (select k.amesto from Akarta k where k.amesto = m and k.alet = l)")
	List<Amesto> getSlobodnaMestaZaLet(@Param("let") Alet let, @Param("idKlase") int idKlase);
	
	@Query("select count(m) from Amesto m inner join m.aavion.alets l where l = :let "
			+ "and not exists (select k.amesto from Akarta k where k.amesto = m and k.alet = l)")
	int getBrojSlobodnihMesta(@Param("let") Alet let);
}
