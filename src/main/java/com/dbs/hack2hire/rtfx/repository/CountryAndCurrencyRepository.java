package com.dbs.hack2hire.rtfx.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dbs.hack2hire.rtfx.entity.CountryAndCurrency;

@Repository
public interface CountryAndCurrencyRepository extends JpaRepository<CountryAndCurrency, Serializable>{
	
}
