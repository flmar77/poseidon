package com.poseidon.app.dal.repository;

import com.poseidon.app.dal.entity.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TradeRepository extends JpaRepository<TradeEntity, Integer> {
    Optional<TradeEntity> findByAccount(String account);
}
