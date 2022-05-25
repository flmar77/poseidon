package com.poseidon.app.unittests.web.apiController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poseidon.app.dal.entity.TradeEntity;
import com.poseidon.app.domain.service.TradeService;
import com.poseidon.app.domain.service.UserService;
import com.poseidon.app.helper.Faker;
import com.poseidon.app.web.apiController.TradeRestController;
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

@WebMvcTest(controllers = TradeRestController.class)
public class TradeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    @SuppressWarnings("unused")
    @MockBean
    private UserService userService;
    @SuppressWarnings("unused")
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void should_returnRightTradeList_whenGetTrade() throws Exception {
        when(tradeService.getAllTrades()).thenReturn(Collections.singletonList(Faker.getFakeTradeEntity()));

        mockMvc.perform(get("/api/trade"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(Faker.getFakeTradeEntity().getAccount()))));
    }

    @Test
    public void should_returnRightTrade_whenGetTradeId() throws Exception {
        when(tradeService.getTradeById(anyInt())).thenReturn(Faker.getFakeTradeEntity());

        mockMvc.perform(get("/api/trade/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(Faker.getFakeTradeEntity().getAccount()))));
    }

    @Test
    public void should_returnNotFound_whenGetMissingTradeId() throws Exception {
        when(tradeService.getTradeById(anyInt())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/api/trade/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("error")));
    }

    @Test
    public void should_returnBindingErrors_whenPostPartialTrade() throws Exception {
        TradeEntity tradeEntityPartial = Faker.getFakeTradeEntity();
        tradeEntityPartial.setAccount("");
        String inputJson = new ObjectMapper().writeValueAsString(tradeEntityPartial);

        mockMvc.perform(post("/api/trade")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_createTrade_whenPostNewTrade() throws Exception {
        when(tradeService.createTrade(any())).thenReturn(Faker.getFakeTradeEntity());
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeTradeEntity());

        mockMvc.perform(post("/api/trade")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void should_notCreateTrade_whenPostExistingTrade() throws Exception {
        when(tradeService.createTrade(any(TradeEntity.class))).thenThrow(EntityExistsException.class);
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeTradeEntity());

        mockMvc.perform(post("/api/trade")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_returnBindingErrors_whenPutPartialTrade() throws Exception {
        TradeEntity tradeEntityPartial = Faker.getFakeTradeEntity();
        tradeEntityPartial.setAccount("");
        String inputJson = new ObjectMapper().writeValueAsString(tradeEntityPartial);

        mockMvc.perform(put("/api/trade/1")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_returnUnprocessableEntity_whenPutInconsistentTrade() throws Exception {
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeTradeEntity());

        mockMvc.perform(put("/api/trade/2")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_updateTrade_whenPutExistingTrade() throws Exception {
        when(tradeService.updateTrade(any())).thenReturn(Faker.getFakeTradeEntity());
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeTradeEntity());

        mockMvc.perform(put("/api/trade/1")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void should_notUpdateTrade_whenPutMissingTrade() throws Exception {
        when(tradeService.updateTrade(any())).thenThrow(NoSuchElementException.class);
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeTradeEntity());

        mockMvc.perform(put("/api/trade/1")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_deleteTrade_whenDeleteExistingTrade() throws Exception {
        mockMvc.perform(delete("/api/trade/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_NotDeleteTrade_whenDeleteMissingTrade() throws Exception {
        doThrow(new NoSuchElementException()).when(tradeService).deleteTradeById(anyInt());

        mockMvc.perform(delete("/api/trade/1"))
                .andExpect(status().isNotFound());
    }

}
