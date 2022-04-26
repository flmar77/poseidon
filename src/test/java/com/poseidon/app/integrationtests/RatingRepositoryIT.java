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
        RatingEntity ratingEntity = new RatingEntity(10, "Moodys Rating", "Sand PRating", "Fitch Rating");

        // Save
        ratingEntity = ratingRepository.save(ratingEntity);
        assertNotNull(ratingEntity.getId());
        assertEquals(10, (int) ratingEntity.getOrderNumber());

        // Update
        ratingEntity.setOrderNumber(20);
        ratingEntity = ratingRepository.save(ratingEntity);
        assertEquals(20, (int) ratingEntity.getOrderNumber());

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
