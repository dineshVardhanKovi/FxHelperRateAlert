package com.dbs.hack2hire.rtfx.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.dbs.hack2hire.rtfx.constants.RightTimeToFXConstants;
import com.dbs.hack2hire.rtfx.response.CountryAndCurrencyResponse;
import com.dbs.hack2hire.rtfx.service.CountryAndCurrencyService;;

@RestController
@RequestMapping(RightTimeToFXConstants.basePath)
public class CountryCurrencyController {
	Logger logger = LoggerFactory.getLogger(CountryCurrencyController.class);

	@Autowired
	private CountryAndCurrencyService countryAndCurrencyService;
	
	@CrossOrigin
	@RequestMapping(value = RightTimeToFXConstants.getCountriesAndCurrencies,method = RequestMethod.GET)
	public ResponseEntity<CountryAndCurrencyResponse> getCountriesAndCurrencies() {
		logger.info("IN CountryCurrencyController.getCountriesAndCurrencies ");
		ResponseEntity<CountryAndCurrencyResponse> responseEntity ;
		try {
			CountryAndCurrencyResponse countryAndCurrencyResponse = new CountryAndCurrencyResponse();
			countryAndCurrencyResponse = countryAndCurrencyService.getCountriesAndCurrencies();
			responseEntity = new ResponseEntity<CountryAndCurrencyResponse>(countryAndCurrencyResponse,HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception occured"  + e.getMessage());
			responseEntity = new ResponseEntity<CountryAndCurrencyResponse>(HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return responseEntity;
	}	
}
