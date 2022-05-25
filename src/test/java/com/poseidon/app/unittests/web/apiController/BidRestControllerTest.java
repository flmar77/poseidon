package com.poseidon.app.unittests.web.apiController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poseidon.app.dal.entity.BidEntity;
import com.poseidon.app.domain.service.BidService;
import com.poseidon.app.domain.service.UserService;
import com.poseidon.app.helper.Faker;
import com.poseidon.app.web.apiController.BidRestController;
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

@WebMvcTest(controllers = BidRestController.class)
public class BidRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BidService bidService;

    @SuppressWarnings("unused")
    @MockBean
    private UserService userService;
    @SuppressWarnings("unused")
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void should_returnRightBidList_whenGetBid() throws Exception {
        when(bidService.getAllBids()).thenReturn(Collections.singletonList(Faker.getFakeBidEntity()));

        mockMvc.perform(get("/api/bid"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(Faker.getFakeBidEntity().getAccount()))));
    }

    @Test
    public void should_returnRightBid_whenGetBidId() throws Exception {
        when(bidService.getBidById(anyInt())).thenReturn(Faker.getFakeBidEntity());

        mockMvc.perform(get("/api/bid/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(Faker.getFakeBidEntity().getAccount()))));
    }

    @Test
    public void should_returnNotFound_whenGetMissingBidId() throws Exception {
        when(bidService.getBidById(anyInt())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/api/bid/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("error")));
    }

    @Test
    public void should_returnBindingErrors_whenPostPartialBid() throws Exception {
        BidEntity bidEntityPartial = Faker.getFakeBidEntity();
        bidEntityPartial.setAccount("");
        String inputJson = new ObjectMapper().writeValueAsString(bidEntityPartial);

        mockMvc.perform(post("/api/bid")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_createBid_whenPostNewBid() throws Exception {
        when(bidService.createBid(any())).thenReturn(Faker.getFakeBidEntity());
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeBidEntity());

        mockMvc.perform(post("/api/bid")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void should_notCreateBid_whenPostExistingBid() throws Exception {
        when(bidService.createBid(any(BidEntity.class))).thenThrow(EntityExistsException.class);
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeBidEntity());

        mockMvc.perform(post("/api/bid")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_returnBindingErrors_whenPutPartialBid() throws Exception {
        BidEntity bidEntityPartial = Faker.getFakeBidEntity();
        bidEntityPartial.setAccount("");
        String inputJson = new ObjectMapper().writeValueAsString(bidEntityPartial);

        mockMvc.perform(put("/api/bid/1")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_returnUnprocessableEntity_whenPutInconsistentBid() throws Exception {
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeBidEntity());

        mockMvc.perform(put("/api/bid/2")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_updateBid_whenPutExistingBid() throws Exception {
        when(bidService.updateBid(any())).thenReturn(Faker.getFakeBidEntity());
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeBidEntity());

        mockMvc.perform(put("/api/bid/1")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void should_notUpdateBid_whenPutMissingBid() throws Exception {
        when(bidService.updateBid(any())).thenThrow(NoSuchElementException.class);
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeBidEntity());

        mockMvc.perform(put("/api/bid/1")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_deleteBid_whenDeleteExistingBid() throws Exception {
        mockMvc.perform(delete("/api/bid/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_NotDeleteBid_whenDeleteMissingBid() throws Exception {
        doThrow(new NoSuchElementException()).when(bidService).deleteBidById(anyInt());

        mockMvc.perform(delete("/api/bid/1"))
                .andExpect(status().isNotFound());
    }

}
