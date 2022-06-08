package com.example.aerodrom.converters;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

import com.example.aerodrom.repository.LetRepository;

import model.Alet;

public class LetConverter implements Converter<String, Alet>{
	
	LetRepository lr;
	
	public LetConverter(LetRepository lr) {
		// TODO Auto-generated constructor stub
		this.lr = lr;
	}

	@Override
	public Alet convert(String source) {
		
		int idL = -1;
		
		try {
			idL = Integer.parseInt(source);
		} catch (NumberFormatException e) {
			throw new ConversionFailedException(TypeDescriptor.valueOf(String.class),
												TypeDescriptor.valueOf(Alet.class), idL, null);
		}
		
		return lr.findById(idL).get();
	}

}
