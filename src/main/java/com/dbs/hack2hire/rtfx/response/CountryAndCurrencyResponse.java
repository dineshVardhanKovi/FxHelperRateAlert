package com.dbs.hack2hire.rtfx.response;

import java.util.List;

import com.dbs.hack2hire.rtfx.entity.CountryAndCurrency;

public class CountryAndCurrencyResponse {
	private List<CountryAndCurrency> countriesAndCurrencies;

	public List<CountryAndCurrency> getCountriesAndCurrencies() {
		return countriesAndCurrencies;
	}

	public void setCountriesAndCurrencies(List<CountryAndCurrency> countriesAndCurrencies) {
		this.countriesAndCurrencies = countriesAndCurrencies;
	}
	
}
