package com.dbs.hack2hire.rtfx.dto;

import java.sql.Date;

public class ExchangeRateDTO {
	private Date date;
	
	private String baseCurrency;
	
	private String exchangeCurreny;
	
	private Double exchangeRate;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public String getExchangeCurreny() {
		return exchangeCurreny;
	}

	public void setExchangeCurreny(String exchangeCurreny) {
		this.exchangeCurreny = exchangeCurreny;
	}

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public ExchangeRateDTO(Date date, String baseCurrency, String exchangeCurreny, Double exchangeRate) {
		this.date = date;
		this.baseCurrency = baseCurrency;
		this.exchangeCurreny = exchangeCurreny;
		this.exchangeRate = exchangeRate;
	}
	
	
}
