package com.poseidon.app.dal.repository;

import com.poseidon.app.dal.entity.RuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RuleRepository extends JpaRepository<RuleEntity, Integer> {
}
