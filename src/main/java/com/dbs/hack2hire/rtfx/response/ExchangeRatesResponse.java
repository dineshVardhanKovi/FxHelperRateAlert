package com.dbs.hack2hire.rtfx.response;

import java.util.List;

import com.dbs.hack2hire.rtfx.dto.ExchangeRateDTO;
import com.dbs.hack2hire.rtfx.entity.ExchangeRates;

public class ExchangeRatesResponse {
	private List<ExchangeRateDTO> exchangeRates;

	public List<ExchangeRateDTO> getExchangeRates() {
		return exchangeRates;
	}

	public void setExchangeRates(List<ExchangeRateDTO> exchangeRates) {
		this.exchangeRates = exchangeRates;
	}



	


	
}
