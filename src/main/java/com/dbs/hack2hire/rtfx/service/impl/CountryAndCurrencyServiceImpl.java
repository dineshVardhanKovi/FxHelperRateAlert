package com.dbs.hack2hire.rtfx.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbs.hack2hire.rtfx.entity.CountryAndCurrency;
import com.dbs.hack2hire.rtfx.repository.CountryAndCurrencyRepository;
import com.dbs.hack2hire.rtfx.response.CountryAndCurrencyResponse;
import com.dbs.hack2hire.rtfx.service.CountryAndCurrencyService;

@Service
public class CountryAndCurrencyServiceImpl implements CountryAndCurrencyService{

	Logger logger = LoggerFactory.getLogger(CountryAndCurrencyServiceImpl.class);
	
	@Autowired
	CountryAndCurrencyRepository countryAndCurrencyRepository;
	
	@Override
	public CountryAndCurrencyResponse getCountriesAndCurrencies() {	
		logger.info("IN CountryAndCurrencyServiceImpl.getCountriesAndCurrencies ");
		CountryAndCurrencyResponse countryAndCurrencyResponse = new CountryAndCurrencyResponse();

		try {
			List<CountryAndCurrency> countriesAndCurrencies = countryAndCurrencyRepository.findAll();
			countryAndCurrencyResponse.setCountriesAndCurrencies(countriesAndCurrencies);
		} catch (Exception e) {
			logger.error("Exception occured"  + e.getMessage());
			throw e;
		}
		return countryAndCurrencyResponse;
	}

}
