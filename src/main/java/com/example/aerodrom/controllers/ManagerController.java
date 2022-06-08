package com.example.aerodrom.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.aerodrom.repository.KartaRepository;
import com.example.aerodrom.repository.LetRepository;
import com.example.aerodrom.repository.RezervacijaRepository;
import com.example.aerodrom.repository.StatusLetaRepository;

import model.Akarta;
import model.Alet;
import model.Arezervacija;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
@RequestMapping(value = "/manager")
public class ManagerController {
	
	@Autowired
	StatusLetaRepository slr;
	
	@Autowired 
	RezervacijaRepository rr;
	
	@Autowired
	LetRepository lr;
	
	@Autowired
	KartaRepository kr;
	
	@Autowired
	JavaMailSender jms;
	
	/*
	 * Metod suspendujLet dobavlja let na osnovu prosledjenog ID-a, zatim dobavlja sve rezervacije za taj let.
	 * Potom za svaku rezervaciju iz baze brise sve karte a na kraju i samu rezervaciju, kada se sve to obavi
	 * aplikacija salje mejl svim putnicima koji su rezervisali kartu za taj let sa informacijom o suspendovanju leta.
	 * Na kraju se let suspenduje.
	 * */
	@RequestMapping(value = "/suspendujLet", method = RequestMethod.POST)
	public String suspendujLet(HttpServletRequest request, Integer letZaSuspendovanje) {
		
		Alet izabranLet = lr.findById(letZaSuspendovanje).get();
		
		List<Arezervacija> rezervacije = rr.findByAlet(izabranLet);
		List<String> emails = new ArrayList<>();
		for (Arezervacija r : rezervacije) {
			emails.add(r.getAputnik().getEmail());
			for (Akarta k : r.getAkartas())
				kr.deleteById(k.getId());
			rr.deleteById(r.getId());
		}
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("filip.vasic2000@gmail.com");
		message.setSubject("Otkazana rezervacija");
		
		String text = "Postovani pozdrav,\n\nLet " + izabranLet.getPolazniGrad().getGrad() + " - " 
					+ izabranLet.getDestinacija().getGrad() + " zakazan za " + izabranLet.getVremePolaska().toString()
					+ " za koji imate rezervaciju je otkazan.";
		message.setText(text);
		
		for (String email : emails) {
			message.setTo(email);			
			jms.send(message);
		}
		
		izabranLet.setAstatusleta(slr.findByStatus("suspendovan"));
		lr.save(izabranLet);
		request.setAttribute("porukaUspeh", "Let je uspesno suspendovan");
		
		return "SuspendovanjeLeta";
	}
	
	
	@RequestMapping(value = "/sveRezervacije.pdf", method = RequestMethod.GET)
	public void izvestajRezervacije(HttpServletRequest request, HttpServletResponse response, Integer idLeta) throws JRException, IOException {
		Alet let = lr.findById(idLeta).get();
		List<Arezervacija> rezervacije = rr.findByAlet(let);
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rezervacije);
		InputStream inputStream = this.getClass().getResourceAsStream("/jasperreports/letRezervacije.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		Map<String, Object> params = new HashMap<>();
		params.put("idLeta", let.getId());
		params.put("let", let.getPolazniGrad().getGrad() + " - " + let.getDestinacija().getGrad());
		params.put("vremePolaska", let.getVremePolaska());
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
		inputStream.close();

		response.setContentType("application/x-download");
		response.addHeader("Content-disposition", "atachment; filename=rezervacije.pdf");
		OutputStream outputStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
		
	}	
}
