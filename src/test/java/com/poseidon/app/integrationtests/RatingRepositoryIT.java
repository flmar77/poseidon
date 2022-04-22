package com.poseidon.app.integrationtests;

import com.poseidon.app.dal.entity.RatingEntity;
import com.poseidon.app.dal.repository.RatingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RatingRepositoryIT {

    @Autowired
    private RatingRepository ratingRepository;

    @Test
    public void ratingTest() {
        RatingEntity ratingEntity = new RatingEntity("Moodys Rating", "Sand PRating", "Fitch Rating", 10);

        // Save
        ratingEntity = ratingRepository.save(ratingEntity);
        assertNotNull(ratingEntity.getId());
        assertTrue(ratingEntity.getOrderNumber() == 10);

        // Update
        ratingEntity.setOrderNumber(20);
        ratingEntity = ratingRepository.save(ratingEntity);
        assertTrue(ratingEntity.getOrderNumber() == 20);

        // Find
        List<RatingEntity> listResult = ratingRepository.findAll();
        assertTrue(listResult.size() > 0);

        // Delete
        Integer id = ratingEntity.getId();
        ratingRepository.delete(ratingEntity);
        Optional<RatingEntity> ratingList = ratingRepository.findById(id);
        assertFalse(ratingList.isPresent());
    }
}
