package com.example.aerodrom.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.aerodrom.repository.AerodromKorisnikRepository;

import model.AaerodromAccount;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	
	@Autowired
	private AerodromKorisnikRepository akr;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AaerodromAccount user = akr.findByUsername(username);
		UserDetailsImpl userDetails = new UserDetailsImpl();
		if (user == null) throw new UsernameNotFoundException("null");
		userDetails.setUsername(user.getUsername());
		userDetails.setPassword(user.getPassword());
		userDetails.setRoles(user.getAaerodromRoles());
		
		return userDetails;
	}

}
