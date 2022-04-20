package com.nnk.springboot.dal.repository;

import com.nnk.springboot.dal.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

}
