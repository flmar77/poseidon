package com.nnk.springboot.dal.repository;

import com.nnk.springboot.dal.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TradeRepository extends JpaRepository<Trade, Integer> {
}
