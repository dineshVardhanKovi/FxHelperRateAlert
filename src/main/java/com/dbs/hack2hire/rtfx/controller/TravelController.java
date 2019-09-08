package com.dbs.hack2hire.rtfx.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.hack2hire.rtfx.constants.RightTimeToFXConstants;
import com.dbs.hack2hire.rtfx.dto.TravelDTO;
import com.dbs.hack2hire.rtfx.response.ExchangeRatesResponse;
import com.dbs.hack2hire.rtfx.service.TravelService;

@RestController
@RequestMapping(RightTimeToFXConstants.basePath)
public class TravelController {
	
	Logger logger = LoggerFactory.getLogger(TravelController.class);

	@Autowired
	private TravelService travelService;
	
	@CrossOrigin
	@RequestMapping(value = RightTimeToFXConstants.getExchangeRates,method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<ExchangeRatesResponse> getExchangeRates(@RequestBody TravelDTO travel) {
		logger.info("IN TravelController.getExchangeRates ");
		ResponseEntity<ExchangeRatesResponse> responseEntity = new ResponseEntity<>(HttpStatus.OK);
		try {
			ExchangeRatesResponse exchangeRatesResponse = travelService.getExchangeRates(travel);
			responseEntity = new ResponseEntity<ExchangeRatesResponse>(exchangeRatesResponse, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception occured"  + e.getMessage());
			responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
}
