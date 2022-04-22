package com.poseidon.app.dal.repository;

import com.poseidon.app.dal.entity.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TradeRepository extends JpaRepository<TradeEntity, Integer> {
}
