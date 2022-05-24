package com.poseidon.app.unittests.web.apiController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poseidon.app.dal.entity.RatingEntity;
import com.poseidon.app.domain.service.RatingService;
import com.poseidon.app.domain.service.UserService;
import com.poseidon.app.helper.Faker;
import com.poseidon.app.web.apiController.RatingRestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityExistsException;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RatingRestController.class)
public class RatingRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService ratingService;

    @SuppressWarnings("unused")
    @MockBean
    private UserService userService;
    @SuppressWarnings("unused")
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void should_returnRightRatingList_whenGetRating() throws Exception {
        when(ratingService.getAllRatings()).thenReturn(Collections.singletonList(Faker.getFakeRatingEntity()));

        mockMvc.perform(get("/api/rating"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(Faker.getFakeRatingEntity().getOrderNumber()))));
    }

    @Test
    public void should_returnRightRating_whenGetRatingId() throws Exception {
        when(ratingService.getRatingById(anyInt())).thenReturn(Faker.getFakeRatingEntity());

        mockMvc.perform(get("/api/rating/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(Faker.getFakeRatingEntity().getOrderNumber()))));
    }

    @Test
    public void should_returnNotFound_whenGetMissingRatingId() throws Exception {
        when(ratingService.getRatingById(anyInt())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/api/rating/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("error")));
    }

    @Test
    public void should_returnBindingErrors_whenPostPartialRating() throws Exception {
        RatingEntity ratingEntityPartial = Faker.getFakeRatingEntity();
        ratingEntityPartial.setOrderNumber(null);
        String inputJson = new ObjectMapper().writeValueAsString(ratingEntityPartial);

        mockMvc.perform(post("/api/rating")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_createRating_whenPostNewRating() throws Exception {
        when(ratingService.createRating(any())).thenReturn(Faker.getFakeRatingEntity());
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeRatingEntity());

        mockMvc.perform(post("/api/rating")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void should_notCreateRating_whenPostExistingRating() throws Exception {
        when(ratingService.createRating(any(RatingEntity.class))).thenThrow(EntityExistsException.class);
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeRatingEntity());

        mockMvc.perform(post("/api/rating")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_returnBindingErrors_whenPutPartialRating() throws Exception {
        RatingEntity ratingEntityPartial = Faker.getFakeRatingEntity();
        ratingEntityPartial.setOrderNumber(null);
        String inputJson = new ObjectMapper().writeValueAsString(ratingEntityPartial);

        mockMvc.perform(put("/api/rating/1")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_returnUnprocessableEntity_whenPutInconsistentRating() throws Exception {
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeRatingEntity());

        mockMvc.perform(put("/api/rating/2")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_updateRating_whenPutExistingRating() throws Exception {
        when(ratingService.updateRating(any())).thenReturn(Faker.getFakeRatingEntity());
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeRatingEntity());

        mockMvc.perform(put("/api/rating/1")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void should_notUpdateRating_whenPutMissingRating() throws Exception {
        when(ratingService.updateRating(any())).thenThrow(NoSuchElementException.class);
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeRatingEntity());

        mockMvc.perform(put("/api/rating/1")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_deleteRating_whenDeleteExistingRating() throws Exception {
        mockMvc.perform(delete("/api/rating/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_NotDeleteRating_whenDeleteMissingRating() throws Exception {
        doThrow(new NoSuchElementException()).when(ratingService).deleteRatingById(anyInt());

        mockMvc.perform(delete("/api/rating/1"))
                .andExpect(status().isNotFound());
    }

}
