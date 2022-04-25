package com.poseidon.app.dal.repository;

import com.poseidon.app.dal.entity.RuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RuleRepository extends JpaRepository<RuleEntity, Integer> {
    Optional<RuleEntity> findByName(String name);
}
