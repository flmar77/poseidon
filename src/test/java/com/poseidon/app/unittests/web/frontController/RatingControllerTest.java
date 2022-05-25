package com.poseidon.app.unittests.web.frontController;

import com.poseidon.app.dal.entity.RatingEntity;
import com.poseidon.app.domain.service.RatingService;
import com.poseidon.app.domain.service.UserService;
import com.poseidon.app.helper.Faker;
import com.poseidon.app.web.frontController.RatingController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityExistsException;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RatingController.class)
public class RatingControllerTest {

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
    @WithMockUser
    public void should_returnRightRatingList_whenGetRatingList() throws Exception {
        when(ratingService.getAllRatings()).thenReturn(Collections.singletonList(Faker.getFakeRatingEntity()));

        mockMvc.perform(get("/rating/list")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/rating/list"))
                .andExpect(model().attributeExists("ratingEntities"))
                .andExpect(content().string(containsString(String.valueOf(Faker.getFakeRatingEntity().getOrderNumber()))));
    }

    @Test
    @WithMockUser
    public void should_returnRightRatingAdd_whenGetRatingAdd() throws Exception {
        mockMvc.perform(get("/rating/add")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ratingEntity"))
                .andExpect(view().name("/rating/add"));
    }

    @Test
    @WithMockUser
    public void should_returnBindingErrors_whenPostPartialRatingAdd() throws Exception {
        RatingEntity ratingEntityPartial = Faker.getFakeRatingEntity();
        ratingEntityPartial.setOrderNumber(-1);

        mockMvc.perform(post("/rating/add")
                        .with(csrf())
                        .flashAttr("ratingEntity", ratingEntityPartial))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("ratingEntity"))
                .andExpect(view().name("/rating/add"));
    }

    @Test
    @WithMockUser
    public void should_createRating_whenPostNewRatingAdd() throws Exception {
        when(ratingService.createRating(any())).thenReturn(Faker.getFakeRatingEntity());

        mockMvc.perform(post("/rating/add")
                        .with(csrf())
                        .flashAttr("ratingEntity", Faker.getFakeRatingEntity()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("rightCreatedRating", true))
                .andExpect(view().name("redirect:/rating/list"));
    }

    @Test
    @WithMockUser
    public void should_notCreateRating_whenPostExistingRatingAdd() throws Exception {
        when(ratingService.createRating(any(RatingEntity.class))).thenThrow(EntityExistsException.class);

        mockMvc.perform(post("/rating/add")
                        .with(csrf())
                        .flashAttr("ratingEntity", Faker.getFakeRatingEntity()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("wrongCreatedRating", true))
                .andExpect(view().name("/rating/add"));
    }

    @Test
    @WithMockUser
    public void should_returnRatingUpdate_whenGetExistingRatingUpdate() throws Exception {
        when(ratingService.getRatingById(anyInt())).thenReturn(Faker.getFakeRatingEntity());

        mockMvc.perform(get("/rating/update/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/rating/update"));
    }

    @Test
    @WithMockUser
    public void should_notReturnRatingUpdate_whenGetMissingRatingUpdate() throws Exception {
        when(ratingService.getRatingById(anyInt())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/rating/update/1")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("missingRatingId", true))
                .andExpect(view().name("redirect:/rating/list"));
    }

    @Test
    @WithMockUser
    public void should_returnBindingErrors_whenPostPartialRatingUpdate() throws Exception {
        RatingEntity ratingEntityPartial = Faker.getFakeRatingEntity();
        ratingEntityPartial.setOrderNumber(-1);

        mockMvc.perform(post("/rating/update/1")
                        .with(csrf())
                        .flashAttr("ratingEntity", ratingEntityPartial))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("ratingEntity"))
                .andExpect(view().name("/rating/update"));
    }

    @Test
    @WithMockUser
    public void should_updateRating_whenPostExistingRatingUpdate() throws Exception {
        when(ratingService.updateRating(any())).thenReturn(Faker.getFakeRatingEntity());

        mockMvc.perform(post("/rating/update/1")
                        .with(csrf())
                        .flashAttr("ratingEntity", Faker.getFakeRatingEntity()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("rightUpdatedRating", true))
                .andExpect(view().name("/rating/update"));
    }

    @Test
    @WithMockUser
    public void should_notUpdateRating_whenPostMissingRatingUpdate() throws Exception {
        when(ratingService.updateRating((any(RatingEntity.class)))).thenThrow(NoSuchElementException.class);

        mockMvc.perform(post("/rating/update/1")
                        .with(csrf())
                        .flashAttr("ratingEntity", Faker.getFakeRatingEntity()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("missingRatingId", true))
                .andExpect(view().name("redirect:/rating/list"));
    }

    @Test
    @WithMockUser
    public void should_deleteRating_whenGetExistingRatingDelete() throws Exception {
        mockMvc.perform(get("/rating/delete/1")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("rightDeletedRating", true))
                .andExpect(view().name("redirect:/rating/list"));
    }

    @Test
    @WithMockUser
    public void should_notDeleteRating_whenGetMissingRatingDelete() throws Exception {
        doThrow(NoSuchElementException.class).when(ratingService).deleteRatingById(anyInt());

        mockMvc.perform(get("/rating/delete/1")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("missingRatingId", true))
                .andExpect(view().name("redirect:/rating/list"));
    }

}
