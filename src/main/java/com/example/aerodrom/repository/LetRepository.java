package com.example.aerodrom.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import model.Agrad;
import model.Alet;
import model.Astatusleta;

public interface LetRepository extends JpaRepository<Alet, Integer> {

	@Query("select l from Alet l where l.polazniGrad = :from and l.destinacija = :to and l.vremePolaska between :dateF and :dateT order by l.vremePolaska")
	List<Alet> findLetFromToBetweenTwoDates(@Param("from") Agrad from, @Param("to") Agrad to, 
											@Param("dateF") Date dateFrom, @Param("dateT") Date dateTo);
	
	List<Alet> findByPolazniGradOrderByVremePolaskaAsc(Agrad grad);
	
	List<Alet> findByDestinacijaOrderByVremePolaskaAsc(Agrad grad);

}
