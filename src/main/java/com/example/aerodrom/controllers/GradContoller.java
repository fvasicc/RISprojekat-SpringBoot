package com.example.aerodrom.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.aerodrom.repository.GradRepository;

import model.Agrad;

@Controller
@RequestMapping(value = "/destinacije")
public class GradContoller {

	@Autowired
	GradRepository gr;
	
	@ModelAttribute("gradovi")
	public List<Agrad> getGradovi(){
		return gr.findAll();
	}
	
	@RequestMapping(value = "/prikazDestinacija", method = RequestMethod.GET)
	public String prikaziDestinacije() {
		return "PrikaziDestinacije";
	}
}
