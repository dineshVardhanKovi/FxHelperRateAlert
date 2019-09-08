package com.dbs.hack2hire.rtfx.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbs.hack2hire.rtfx.dto.ExchangeRateDTO;
import com.dbs.hack2hire.rtfx.dto.TravelDTO;
import com.dbs.hack2hire.rtfx.entity.ExchangeRates;
import com.dbs.hack2hire.rtfx.repository.ExchangeRatesRepository;
import com.dbs.hack2hire.rtfx.response.ExchangeRatesResponse;
import com.dbs.hack2hire.rtfx.service.TravelService;

@Service
public class TravelServiceImpl implements TravelService{
	
	Logger logger = LoggerFactory.getLogger(CountryAndCurrencyServiceImpl.class);
	
	@Autowired
	private ExchangeRatesRepository exchangeRatesRepository;

	@Override
	public ExchangeRatesResponse getExchangeRates(TravelDTO travel) {
		logger.info("IN ExchangeRatesResponse.getExchangeRates ");
		ExchangeRatesResponse exchangeRatesResponse = new ExchangeRatesResponse();
		List<ExchangeRateDTO> exchangeRateDTOs = new ArrayList<ExchangeRateDTO>();
		try {
			List<ExchangeRates> exchangeRates = exchangeRatesRepository.getExchangeRates(travel.getFromDate(), travel.getToDate());
			exchangeRates.forEach(exchangeRate -> {
				ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO(exchangeRate.getCurDate(), travel.getBaseCurrency(), 
						travel.getExchangeCurrency(), exchangeRate.getExchangeRate());
				exchangeRateDTOs.add(exchangeRateDTO);
			});
			exchangeRatesResponse.setExchangeRates(exchangeRateDTOs);
		}catch (Exception e) {
			logger.error("Exception occured"  + e.getMessage());
			throw e;
		}
		return exchangeRatesResponse;
	}

}
