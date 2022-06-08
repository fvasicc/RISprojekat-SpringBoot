package com.example.aerodrom.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.aerodrom.repository.AerodromKorisnikRepository;
import com.example.aerodrom.repository.AerodromUlogaRepository;
import com.example.aerodrom.repository.PutnikRepository;

import model.AaerodromAccount;
import model.AaerodromRole;
import model.Aputnik;

@Controller
@ControllerAdvice
@RequestMapping(value = "/auth")
public class LoginController {

	@Autowired
	AerodromKorisnikRepository akr;

	@Autowired
	AerodromUlogaRepository aur;

	@Autowired
	PutnikRepository pr;

	@RequestMapping(value = "loginPage", method = RequestMethod.GET)
	public String loginPage() {
		return "login";
	}

	@ModelAttribute("user")
	public Aputnik getAccount() {
		return new Aputnik();
	}

	@RequestMapping(value = "registerUser", method = RequestMethod.GET)
	public String newUser() {
		return "register";
	}
	
	private String neuspelaRegistracija = null;

	private boolean valid(String email, String username, String jmbg) {
		
		AaerodromAccount acc = akr.findByUsername(username);
		Aputnik putnik = pr.findByEmail(email);
		if (acc != null) {
			neuspelaRegistracija = "Registracija nije uspela. Korisnicko ime " + username + " je zauzeto!";
			return false;
		} else if (putnik != null) {
			neuspelaRegistracija = "Registracija nije uspela. Vec postoji korisnik sa unetom email adresom!";
			return false;
		} else if (jmbg.length() != 13) {
			neuspelaRegistracija = "Registracija nije uspela. JMBG nije unet ispravno, jmbg nije dobre duzine!";
			return false;
		} else {
			for (int i = 0; i < 13; i++) {
				if (!Character.isDigit(jmbg.charAt(i))) {
					neuspelaRegistracija = "Registracija nije uspela. JMBG nije unet ispravno, u jmbgu smeju biti samo brojevi!";
					return false;
				}
			}
		}
		
		return true;
	}
	
	
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String saveUser(@ModelAttribute("user") Aputnik user, String korisnickoIme, String sifra,
			HttpServletRequest request) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		AaerodromAccount account = new AaerodromAccount();

		if (!valid(user.getEmail(), korisnickoIme, user.getJmbg())) {
			request.setAttribute("porukaNeuspeh", neuspelaRegistracija);
			return "register";
		}
		
		account.setUsername(korisnickoIme);
		account.setPassword(passwordEncoder.encode(sifra));
		
		//rola sa id 2 je USER
		AaerodromRole role = aur.findById(2).get();
		account.getAaerodromRoles().add(role);
		role.getAaerodromAccounts().add(account);
		AaerodromAccount savedAccount = akr.save(account);
		user.setAaerodromAccount(savedAccount);
		pr.save(user);
		request.setAttribute("porukaUspeh", "Uspesno ste se registrovali. Mozete se prijaviti!");
		return "login";
	}

	@RequestMapping(value = "/pocetna", method = RequestMethod.GET)
	public String getPocetna() {
		return "index";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		return "redirect:/auth/loginPage";
	}
}
