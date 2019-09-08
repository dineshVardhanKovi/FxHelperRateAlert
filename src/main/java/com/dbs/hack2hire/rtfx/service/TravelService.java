package com.dbs.hack2hire.rtfx.service;

import com.dbs.hack2hire.rtfx.dto.TravelDTO;
import com.dbs.hack2hire.rtfx.response.ExchangeRatesResponse;

public interface TravelService {
	ExchangeRatesResponse getExchangeRates(TravelDTO travel);
}
