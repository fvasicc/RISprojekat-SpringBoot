package com.example.aerodrom.controllers;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.aerodrom.repository.AerodromKorisnikRepository;
import com.example.aerodrom.repository.KartaRepository;
import com.example.aerodrom.repository.KlasaRepository;
import com.example.aerodrom.repository.LetRepository;
import com.example.aerodrom.repository.MestoRepository;
import com.example.aerodrom.repository.PutnikRepository;
import com.example.aerodrom.repository.RezervacijaRepository;
import com.example.aerodrom.repository.StatusLetaRepository;

import model.AaerodromAccount;
import model.Akarta;
import model.Aklasa;
import model.Alet;
import model.Amesto;
import model.Aputnik;
import model.Arezervacija;

@Controller
@RequestMapping(value = "/rezervacija")
public class RezervacijaController {
	
	@Autowired
	KlasaRepository kr;
	
	@Autowired
	LetRepository lr;
	
	@Autowired
	MestoRepository mr;
	
	@Autowired
	AerodromKorisnikRepository akr;
	
	@Autowired
	PutnikRepository pr;
	
	@Autowired
	KartaRepository kartar;
	
	@Autowired
	RezervacijaRepository rr;
	
	@Autowired
	StatusLetaRepository slr;
	
	@RequestMapping(value = "/rezervisi", method = RequestMethod.GET)
	public String rezervisi(Integer idLet, HttpServletRequest request) {
		
		Alet let = lr.findById(idLet).get();
		
		List<Amesto> slobodnaMestaBiznis = mr.getSlobodnaMestaZaLet(let, 1);
		List<Amesto> slobodnaMestaEcoComfort = mr.getSlobodnaMestaZaLet(let, 2);
		List<Amesto> slobodnaMestaEcoStandrad = mr.getSlobodnaMestaZaLet(let, 3);
		List<Amesto> slobodnaMestaEcoLight = mr.getSlobodnaMestaZaLet(let, 4);
		
		request.getSession().setAttribute("brBiznis", slobodnaMestaBiznis.size());
		request.getSession().setAttribute("brEcoComfort", slobodnaMestaEcoComfort.size());
		request.getSession().setAttribute("brEcoStandard", slobodnaMestaEcoStandrad.size());
		request.getSession().setAttribute("brEcoLight", slobodnaMestaEcoLight.size());
				
		request.getSession().setAttribute("idLet", idLet);
		
		request.getSession().setAttribute("idBiznis", 1);
		request.getSession().setAttribute("idComfort", 2);
		request.getSession().setAttribute("idStandard", 3);
		request.getSession().setAttribute("idLight", 4);
		
		return "SlobodnaMesta";
		
	}
	
	@RequestMapping(value = "/rezervisiLet", method = RequestMethod.GET)
	public String rezervisiLet(Integer idLet, Integer idKlase, HttpServletRequest request) {
		
		Alet let = lr.findById(idLet).get();
		Aklasa klasa = kr.findById(idKlase).get();
		List<Amesto> lista = mr.getSlobodnaMestaZaLet(let, idKlase);
		double cena = let.getCenaLet() + klasa.getCenaKlasa();
		
		request.getSession().setAttribute("lista", lista);
		request.getSession().setAttribute("izabranlet", let);
		request.getSession().setAttribute("cena", cena);
		
		return "SlobodnaMesta";
		
	}
	
	@RequestMapping(value = "/zavrsiRezervaciju", method = RequestMethod.POST)
	public String zavrsiRezervaciju(HttpServletRequest request, Integer brojKarata) {
		
		List<Amesto> mesta = (List<Amesto>) request.getSession().getAttribute("lista");	
		String username = null;
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) 
			username = ((UserDetails)principal).getUsername();
		
		AaerodromAccount acc = akr.findByUsername(username);
		Aputnik p;
		Arezervacija rezervacija = new Arezervacija();
		rezervacija.setDatumRezervacije(new Date());
		
		p = pr.findByAaerodromAccount(acc);
		rezervacija.setAputnik(p);
		
		Arezervacija sacuvanaRezervacija = rr.save(rezervacija);
		String poruka = "Registracija uspesno sacuvana. Vase rezervacije mozete proveriti u odeljku 'moje rezervacije'"
				+ " i preuzeti potvrdu o rezervaciji, sa kojom mozete preuzeti karte na aerodromu.\n"
				+ "Preuzimanje karata moguce je najkasnije 90 minuta pre pocetka leta, nakon toga rezervacije nisu validne. ID: " + sacuvanaRezervacija.getId() + "\nID karata: ";
		Alet izabranLet = (Alet) request.getSession().getAttribute("izabranlet");
		
		
		try {
			for (int i = 0; i < brojKarata; i++) {
				Akarta karta = new Akarta();
				karta.setAlet(izabranLet);
				karta.setCenaKarta((double) request.getSession().getAttribute("cena"));
				karta.setAmesto(mesta.remove(0));
				karta.setArezervacija(sacuvanaRezervacija);
				
				Akarta sacuvanaKarta = kartar.save(karta);
				poruka += sacuvanaKarta.getId() +" ";
				
				rezervacija.getAkartas().add(sacuvanaKarta);
			}
			
			request.setAttribute("poruka", poruka);
		} catch (Exception e) {
			e.printStackTrace();
			for (Akarta k : sacuvanaRezervacija.getAkartas())
				kartar.deleteById(k.getId());
			rr.deleteById(sacuvanaRezervacija.getId());
			request.getSession().setAttribute("porukaNeuspeh", "Rezervacija nije uspesna. Nazalost nema dovljno karata.");
			return "SlobodnaMesta";
		}
		
		int preostalaSlobodnaMesta = mr.getBrojSlobodnihMesta(izabranLet);
		
		if (preostalaSlobodnaMesta == 0) {
			izabranLet.setAstatusleta(slr.findByStatus("rasprodat"));
			lr.save(izabranLet);
		}
		
		request.getSession().removeAttribute("cena");
		request.getSession().setAttribute("porukaUspeh", poruka);
		
		posaljiObavestenjePutniku(p.getEmail(), sacuvanaRezervacija, izabranLet);
		
//		return "SlobodnaMesta";
		return "redirect:/rezervacija/rezervisi?idLet=" + izabranLet.getId();
	}
	
	@Autowired
	JavaMailSender jms;
	
	private void posaljiObavestenjePutniku(String mail, Arezervacija rezervacija, Alet let) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("filip.vasic2000@gmail.com");
		message.setTo(mail);
		message.setSubject("Uspesna rezervacija");
		message.setText("Vasa rezervacija za let " + let.getPolazniGrad().getGrad() + " - " + let.getDestinacija().getGrad()				
				+ " je prihvacena.\n\nID broj vase rezervacije je : " + rezervacija.getId() + 
				"\n\nHvala sto putujete sa nama.");
	
		jms.send(message);
	}
}
