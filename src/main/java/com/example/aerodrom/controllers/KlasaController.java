package com.example.aerodrom.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.aerodrom.repository.KlasaRepository;

import model.Aklasa;

@Controller
@RequestMapping(value = "/klase")
public class KlasaController {
	
	@Autowired
	KlasaRepository kr;
	
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public String getKlase(Integer idKlase, HttpServletRequest request) {
		
		List<Aklasa> klList = kr.findAll();	
		request.setAttribute("klase", klList);
		
		return "InfoKlase";
	}
}
