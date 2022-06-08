package com.example.aerodrom.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import model.Aavion;

public interface AvionRepository extends JpaRepository<Aavion, Integer> {

	@Query("select distinct a from Aavion a where not exists (select aa from Aavion aa inner join"
			+ " aa.alets l where (l.vremePolaska between :p and :s) and (l.vremeSletanja between :p and :s) and aa = a)")
	List<Aavion> findSlobodniAvioni(@Param("p") Date poletanje,@Param("s") Date sletanje);
}
