package com.nnk.springboot.dal.repository;

import com.nnk.springboot.dal.entity.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TradeRepository extends JpaRepository<TradeEntity, Integer> {
}
