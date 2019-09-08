package com.dbs.hack2hire.rtfx.repository;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dbs.hack2hire.rtfx.entity.ExchangeRates;

@Repository
public interface ExchangeRatesRepository extends JpaRepository<ExchangeRates, Serializable> {
	
	@Query(value="select * from (select * from exchange_rates where cur_date >= :fromDate and cur_date <= :toDate order by exchange_rate) where rownum<10" ,nativeQuery= true)
	List<ExchangeRates> getExchangeRates(@Param ("fromDate") Date fromDate,@Param("toDate")Date toDate);
}
