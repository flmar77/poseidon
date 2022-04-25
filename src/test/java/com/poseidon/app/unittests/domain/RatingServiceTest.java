package com.poseidon.app.unittests.domain;


import com.poseidon.app.dal.entity.RatingEntity;
import com.poseidon.app.dal.repository.RatingRepository;
import com.poseidon.app.domain.service.RatingService;
import com.poseidon.app.helper.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    @InjectMocks
    private RatingService ratingService;

    @Mock
    private RatingRepository ratingRepository;

    @Test
    public void should_returnSomething_whenGetAllRatings() {
        when(ratingRepository.findAll()).thenReturn(Collections.singletonList(Faker.getFakeRatingEntity()));

        assertThat(ratingService.getAllRatings()).isNotNull();
    }

    @Test
    public void should_saveRating_whenCreateNewRating() {
        when(ratingRepository.findByOrderNumber(anyInt())).thenReturn(Optional.empty());
        when(ratingRepository.save(any())).thenReturn(Faker.getFakeRatingEntity());

        RatingEntity ratingEntity = ratingService.createRating(Faker.getFakeRatingEntity());

        assertThat(ratingEntity).isNotNull();
        verify(ratingRepository, times(1)).save(any());
    }

    @Test
    public void should_throwEntityExistsException_whenCreateExistingRating() {
        when(ratingRepository.findByOrderNumber(anyInt())).thenReturn(Optional.of(Faker.getFakeRatingEntity()));

        assertThatExceptionOfType(EntityExistsException.class)
                .isThrownBy(() -> ratingService.createRating(Faker.getFakeRatingEntity()));
    }

    @Test
    public void should_findRating_whenGetExistingRatingById() {
        when(ratingRepository.findById(anyInt())).thenReturn(Optional.of(Faker.getFakeRatingEntity()));

        RatingEntity ratingEntity = ratingService.getRatingById(anyInt());

        assertThat(ratingEntity).isNotNull();
        verify(ratingRepository, times(1)).findById(anyInt());
    }

    @Test
    public void should_throwNoSuchElementException_whenGetExistingRatingById() {
        when(ratingRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> ratingService.getRatingById(anyInt()));
    }

    @Test
    public void should_saveRating_whenUpdateExistingRating() {
        when(ratingRepository.findById(any())).thenReturn(Optional.of(Faker.getFakeRatingEntity()));
        when(ratingRepository.save(any())).thenReturn(Faker.getFakeRatingEntity());

        RatingEntity ratingEntity = ratingService.updateRating(Faker.getFakeRatingEntity());

        assertThat(ratingEntity).isNotNull();
        verify(ratingRepository, times(1)).save(any());
    }

    @Test
    public void should_throwNoSuchElementException_whenUpdateMissingRating() {
        when(ratingRepository.findById(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> ratingService.updateRating(Faker.getFakeRatingEntity()));
    }

    @Test
    public void should_deleteRating_whenDeleteExistingRating() {
        when(ratingRepository.findById(anyInt())).thenReturn(Optional.of(Faker.getFakeRatingEntity()));

        ratingService.deleteRatingById(anyInt());

        verify(ratingRepository, times(1)).deleteById(anyInt());
    }

    @Test
    public void should_throwNoSuchElementException_whenDeleteExistingRating() {
        when(ratingRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> ratingService.deleteRatingById(anyInt()));
    }

}
