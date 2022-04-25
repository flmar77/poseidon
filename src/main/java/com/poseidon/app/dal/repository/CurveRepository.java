package com.poseidon.app.dal.repository;

import com.poseidon.app.dal.entity.CurveEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CurveRepository extends JpaRepository<CurveEntity, Integer> {

    Optional<CurveEntity> findByCurveId(Integer curveId);
}
