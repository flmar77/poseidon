package com.nnk.springboot;

import com.nnk.springboot.dal.entity.RatingEntity;
import com.nnk.springboot.dal.repository.RatingRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RatingEntityTests {

	@Autowired
	private RatingRepository ratingRepository;

	@Test
	public void ratingTest() {
		RatingEntity ratingEntity = new RatingEntity("Moodys Rating", "Sand PRating", "Fitch Rating", 10);

		// Save
		ratingEntity = ratingRepository.save(ratingEntity);
		Assert.assertNotNull(ratingEntity.getId());
		Assert.assertTrue(ratingEntity.getOrderNumber() == 10);

		// Update
		ratingEntity.setOrderNumber(20);
		ratingEntity = ratingRepository.save(ratingEntity);
		Assert.assertTrue(ratingEntity.getOrderNumber() == 20);

		// Find
		List<RatingEntity> listResult = ratingRepository.findAll();
		Assert.assertTrue(listResult.size() > 0);

		// Delete
		Integer id = ratingEntity.getId();
		ratingRepository.delete(ratingEntity);
		Optional<RatingEntity> ratingList = ratingRepository.findById(id);
		Assert.assertFalse(ratingList.isPresent());
	}
}
