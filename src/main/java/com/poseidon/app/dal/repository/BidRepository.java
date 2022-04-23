package com.poseidon.app.dal.repository;

import com.poseidon.app.dal.entity.BidEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BidRepository extends JpaRepository<BidEntity, Integer> {

    Optional<BidEntity> findByAccount(String account);
}
