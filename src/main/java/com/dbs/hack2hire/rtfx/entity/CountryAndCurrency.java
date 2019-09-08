package com.dbs.hack2hire.rtfx.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="COUNTRY_AND_CURRENCY")
public class CountryAndCurrency {
	
	@Id
	@Column(name = "country")
	private String country;
	
	@Column(name = "currency")
	private String currency;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public CountryAndCurrency(String country, String currency) {
		this.country = country;
		this.currency = currency;
	}

	public CountryAndCurrency() {
		super();
	}
	
	

	
}
