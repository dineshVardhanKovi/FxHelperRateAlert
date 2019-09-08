package com.dbs.hack2hire.rtfx.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "exchange_rates")
public class ExchangeRates {
	@Id
	@Column(name = "ind")
	private long ind;
	@Column(name = "exchange_rate")
	private Double exchangeRate;
	@Column(name = "cur_date")
	private Date curDate;
	
	public long getInd() {
		return ind;
	}
	public void setInd(long ind) {
		this.ind = ind;
	}
	public Double getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public Date getCurDate() {
		return curDate;
	}
	public void setCurDate(Date curDate) {
		this.curDate = curDate;
	}

	
}
