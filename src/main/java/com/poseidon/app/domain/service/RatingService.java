package com.poseidon.app.domain.service;

import com.poseidon.app.dal.entity.RatingEntity;
import com.poseidon.app.dal.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public List<RatingEntity> getAllRatings() {
        return ratingRepository.findAll();
    }

    public RatingEntity createRating(RatingEntity ratingEntity) throws EntityExistsException {
        if (ratingRepository.findByOrderNumber(ratingEntity.getOrderNumber()).isPresent()) {
            throw new EntityExistsException();
        }
        return saveRating(ratingEntity);
    }

    public RatingEntity getRatingById(Integer id) throws NoSuchElementException {
        return ratingRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public RatingEntity updateRating(RatingEntity ratingEntity) throws NoSuchElementException {
        checkExistenceOfRatingById(ratingEntity.getId());
        return saveRating(ratingEntity);
    }

    public void deleteRatingById(Integer id) throws NoSuchElementException {
        checkExistenceOfRatingById(id);
        ratingRepository.deleteById(id);
    }

    private void checkExistenceOfRatingById(Integer id) {
        ratingRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    private RatingEntity saveRating(RatingEntity ratingEntityToSave) {
        return ratingRepository.save(ratingEntityToSave);
    }
}
