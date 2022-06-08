package com.example.aerodrom.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.aerodrom.repository.AerodromKorisnikRepository;
import com.example.aerodrom.repository.KartaRepository;
import com.example.aerodrom.repository.PutnikRepository;
import com.example.aerodrom.repository.RezervacijaRepository;

import model.AaerodromAccount;
import model.Akarta;
import model.Aputnik;
import model.Arezervacija;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	
	@Autowired
	AerodromKorisnikRepository akr;
	
	@Autowired
	RezervacijaRepository rr;
	
	@Autowired
	PutnikRepository pr;
	
	@Autowired
	KartaRepository kr;
	
	@RequestMapping(value = "/account", method = RequestMethod.GET) 
	public String informacijeONalogu(HttpServletRequest request) {
		
		AaerodromAccount acc = trenutniNalog();
		
		request.setAttribute("acc", pr.findByAaerodromAccount(acc));
		
		return "Nalog";
	}

	@RequestMapping(value = "/promeniLozinku", method = RequestMethod.GET) 
	public String preusmeriNaPromenuLozinke() {
		return "PromenaLozinke";
	}
	
	@RequestMapping(value = "/promenaLozinke", method = RequestMethod.POST)
	public String promenaLozinke(String oldPassword, String newPassword, 
								String repeatPassword, HttpServletRequest request) {
		
		AaerodromAccount acc = trenutniNalog();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(encoder.matches(oldPassword, acc.getPassword())) {
			if (newPassword.equals(repeatPassword)) {
				acc.setPassword(encoder.encode(newPassword));
				akr.save(acc);
				request.setAttribute("porukaUspeh", "Uspesno ste promenili svoju lozinku!");
			} else {
				request.setAttribute("porukaNeuspeh", "Nova lozinka i ponovljena lozinka se ne poklapaju, pokusajte ponovo!");
			}
		} else {
			request.setAttribute("porukaNeuspeh", "Pogresno unesena stara lozinka!");
		}
		
		return "PromenaLozinke";
	}
	
	private AaerodromAccount trenutniNalog() {
		String username = null;
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) 
			username = ((UserDetails)principal).getUsername();
		
		return akr.findByUsername(username);
	}
	
	@RequestMapping(value = "/rezervacijeKorisnika", method = RequestMethod.GET)
	public String prikazRezervacija(HttpServletRequest request) {
		
		AaerodromAccount acc = trenutniNalog();
		
		Aputnik putnik = pr.findByAaerodromAccount(acc);
		
		List<Arezervacija> rezervacije = rr.findByAputnik(putnik);
		
		request.getSession().setAttribute("rezervacijePutnika", rezervacije);
		
		return "Rezervacije";
	}
	
	@RequestMapping(value = "/otkazivanjeRezervacije", method = RequestMethod.GET)
	public String otkaziRezervaciju(HttpServletRequest request, Integer rezervacijaZaUklanjanje) {
		Arezervacija rezervacija = rr.findById(rezervacijaZaUklanjanje).get();
		try {
			for (Akarta karta : rezervacija.getAkartas())
				kr.deleteById(karta.getId());
			
			rr.deleteById(rezervacija.getId());
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("porukaNeuspeh", "Otkazivanje rezervacije nije uspelo.");
			return "Rezervacije";
		}
		
		request.getSession().setAttribute("porukaUspeh", "Rezervacija je uspesno otkazana.");
		return "redirect:/user/rezervacijeKorisnika";
	}
	
	@RequestMapping(value = "/rezervacija.pdf", method = RequestMethod.GET)
	public void rezervacijaIzvestaj(HttpServletRequest request, HttpServletResponse response, Integer izabranaRezervacija) throws JRException, IOException {
		Arezervacija rezervacija = rr.findById(izabranaRezervacija).get();
		List<Akarta> karte = rezervacija.getAkartas();
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(karte);
		InputStream inputStream = this.getClass().getResourceAsStream("/jasperreports/izvestajRezervacija.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		Map<String, Object> params = new HashMap<>();
		AaerodromAccount acc = trenutniNalog();
		Aputnik putnik = pr.findByAaerodromAccount(acc);
		params.put("putnikImePrezime", putnik.getIme() + " " + putnik.getPrezime());
		params.put("emailPutnik", putnik.getEmail());
		params.put("putnikTelefon", putnik.getTelefon());
		params.put("putnikJMBG", putnik.getJmbg());
		params.put("datumRezervacije", rezervacija.getDatumRezervacije());
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
		inputStream.close();

		response.setContentType("application/x-download");
		response.addHeader("Content-disposition", "atachment; filename=rezervacija.pdf");
		OutputStream outputStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
	}
}
