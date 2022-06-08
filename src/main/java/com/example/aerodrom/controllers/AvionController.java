package com.example.aerodrom.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.aerodrom.repository.AvionRepository;
import com.example.aerodrom.repository.KlasaRepository;
import com.example.aerodrom.repository.MestoRepository;

import model.Aavion;
import model.Aklasa;
import model.Amesto;

@Controller
@RequestMapping(value = "/avioni")
public class AvionController {

	@Autowired
	AvionRepository ar;
	
	@Autowired
	MestoRepository mr;
	
	@Autowired
	KlasaRepository kr;
	
	@ModelAttribute("avion")
	public Aavion getAvion() {
		return new Aavion();
	}
	
	@RequestMapping(value = "/preusmeri", method = RequestMethod.GET)
	public String preusmeriNaDodavanjeAviona() {
		return "unos/UnosNovogAviona";
	}
	
	@RequestMapping(value = "/dodavanjeAviona", method = RequestMethod.POST)
	public String dodajAvion(@ModelAttribute("avion") Aavion avion, HttpServletRequest request) {
		
		int brSedista = avion.getBrojSedista();
		Aavion sacuvanAvion = ar.save(avion);
		
		Aklasa biznis = kr.getById(1);
		Aklasa ecoCom = kr.getById(2);
		Aklasa ecoStr = kr.getById(3);
		Aklasa ecoLht = kr.getById(4);
		
		for (int i = 0; i < brSedista; i++) {
			Amesto mesto = new Amesto();
			mesto.setRedniBroj(i + 1);
			mesto.setAavion(sacuvanAvion);
			
			if (i < brSedista / 5)
				mesto.setAklasa(biznis);
			else if (i < brSedista / 3)
				mesto.setAklasa(ecoCom);
			else if (i < brSedista - 100) 
				mesto.setAklasa(ecoStr);
			else
				mesto.setAklasa(ecoLht);
			
			
			Amesto sacuvanoMesto = mr.save(mesto);
			System.out.println("Sacuvano mesto u avionu. ID " + sacuvanoMesto.getId());
			
//			sacuvanAvion.addAmesto(sacuvanoMesto);
		}
		
		request.setAttribute("poruka", "Avion je uspesno sacuvan u bazi. ID aviona je: " + sacuvanAvion.getId());
		
		return "unos/UnosNovogAviona";
	}

}
