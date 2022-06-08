package com.example.aerodrom.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.aerodrom.repository.AvionRepository;
import com.example.aerodrom.repository.GradRepository;
import com.example.aerodrom.repository.LetRepository;
import com.example.aerodrom.repository.MestoRepository;
import com.example.aerodrom.repository.StatusLetaRepository;

import model.Aavion;
import model.Agrad;
import model.Alet;

@Controller
@RequestMapping(value = "/letovi")
public class LetController {

	@Autowired
	GradRepository gr;
	
	@Autowired
	LetRepository lr;
	
	@Autowired
	AvionRepository ar;
	
	@Autowired
	StatusLetaRepository slr;
	
	@Autowired
	MestoRepository mr;
	
	@ModelAttribute("gradovi")
	public List<Agrad> getGradovi() {
		return gr.findAll();
	}
	
	@ModelAttribute("let")
	public Alet getLet() {
		return new Alet();
	}
	
	@RequestMapping(value = "/unosPodataka", method = RequestMethod.GET)
	public String unosNovogLeta() {
		return "unos/UnosNovogLeta";
	}
	
	@RequestMapping(value = "/slobodniAvioni", method = RequestMethod.GET)
	public String getSlobodniAvioni(@ModelAttribute("let") Alet let, Model m, 
								HttpServletRequest request,	Date poletanje, Date sletanje) {
		
		Calendar c = Calendar.getInstance();
		c.setTime(let.getVremePolaska());
		c.add(Calendar.HOUR, -8);
		Date first = c.getTime();
		
		c.setTime(let.getVremeSletanja());
		c.add(Calendar.HOUR, 8);
		Date second = c.getTime();
		
		List<Aavion> slobodniAvioni = ar.findSlobodniAvioni(first, second);
		
		request.setAttribute("avioni", slobodniAvioni);
		request.getSession().setAttribute("let", let);
		
		return "unos/UnosNovogLeta";
	}
	
	@RequestMapping(value = "/sacuvajLet", method = RequestMethod.POST)
	public String sacuvajLet(Integer idAviona,Model m, HttpServletRequest request) {		
		Alet let = (Alet)request.getSession().getAttribute("let");
		let.setAavion(ar.findById(idAviona).get());
		let.setAstatusleta(slr.findByStatus("aktivan"));
		
		Alet sacuvanLet = lr.save(let);
		
		request.setAttribute("poruka", "Let je uspesno sacuvan. Id leta je " + sacuvanLet.getId());
		
		return "unos/UnosNovogLeta";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
		sdf.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}
	
	@RequestMapping(value = "/pretragaLetova", method = RequestMethod.GET)
	public String pokreniPretragu() {
		return "unos/PrikazLetova";
	}
	
	@RequestMapping(value = "/prikaziLetove", method = RequestMethod.GET)
	public String prikaziLetove(Integer idGradOd, Integer idGradDo, Date datumOd, Date datumDo, Model m) {
		
		List<Alet> letovi = lr.findLetFromToBetweenTwoDates(gr.findById(idGradOd).get(), 
															gr.findById(idGradDo).get(), 
															datumOd, datumDo);
		
		m.addAttribute("letovi", letovi);
		
		return "unos/PrikazLetova";
	}
	
	@RequestMapping(value = "/polasci", method = RequestMethod.GET)
	public String polasci(HttpServletRequest request, Model m, Integer idGrad) {
		Agrad grad = gr.findById(idGrad).get();
		List<Alet> polasci = lr.findByPolazniGradOrderByVremePolaskaAsc(grad);
		
		for(int i = polasci.size() - 1; i >= 0; i--) {
			if (polasci.get(i).getVremePolaska().before(new Date()) || polasci.get(i).getAstatusleta().getStatus().equals("suspendovan")) 
				polasci.remove(i);
		}
		
		m.addAttribute("letovi", polasci);
		
		return "unos/PrikazLetova";
	}
	
	@RequestMapping(value = "/dolasci", method = RequestMethod.GET)
	public String dolasci(HttpServletRequest request, Model m, Integer idGrad) {
		Agrad grad = gr.findById(idGrad).get();
		List<Alet> dolasci = lr.findByDestinacijaOrderByVremePolaskaAsc(grad);
		
		for(int i = dolasci.size() - 1; i >= 0; i--) {
			if (dolasci.get(i).getVremePolaska().before(new Date()) || dolasci.get(i).getAstatusleta().getStatus().equals("suspendovan")) 
				dolasci.remove(i);
		}
		
		m.addAttribute("letovi", dolasci);
		
		return "unos/PrikazLetova";
	}
	
	
}