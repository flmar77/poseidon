package com.poseidon.app.dal.repository;

import com.poseidon.app.dal.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<RatingEntity, Integer> {

    Optional<RatingEntity> findByOrderNumber(Integer orderNumber);
}
