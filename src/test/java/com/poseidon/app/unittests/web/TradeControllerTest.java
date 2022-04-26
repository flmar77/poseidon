package com.poseidon.app.unittests.web;

import com.poseidon.app.dal.entity.TradeEntity;
import com.poseidon.app.domain.service.TradeService;
import com.poseidon.app.domain.service.UserService;
import com.poseidon.app.helper.Faker;
import com.poseidon.app.web.frontController.TradeController;
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

@WebMvcTest(controllers = TradeController.class)
public class TradeControllerTest {

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
    @WithMockUser
    public void should_returnRightTradeList_whenGetTradeList() throws Exception {
        when(tradeService.getAllTrades()).thenReturn(Collections.singletonList(Faker.getFakeTradeEntity()));

        mockMvc.perform(get("/trade/list")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/trade/list"))
                .andExpect(model().attributeExists("tradeEntities"))
                .andExpect(content().string(containsString(String.valueOf(Faker.getFakeTradeEntity().getAccount()))));
    }

    @Test
    @WithMockUser
    public void should_returnRightTradeAdd_whenGetTradeAdd() throws Exception {
        mockMvc.perform(get("/trade/add")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("tradeEntity"))
                .andExpect(view().name("/trade/add"));
    }

    @Test
    @WithMockUser
    public void should_returnBindingErrors_whenPostPartialTradeAdd() throws Exception {
        TradeEntity tradeEntityPartial = Faker.getFakeTradeEntity();
        tradeEntityPartial.setAccount("");

        mockMvc.perform(post("/trade/add")
                        .with(csrf())
                        .flashAttr("tradeEntity", tradeEntityPartial))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("tradeEntity"))
                .andExpect(view().name("/trade/add"));
    }

    @Test
    @WithMockUser
    public void should_createTrade_whenPostNewTradeAdd() throws Exception {
        when(tradeService.createTrade(any())).thenReturn(Faker.getFakeTradeEntity());

        mockMvc.perform(post("/trade/add")
                        .with(csrf())
                        .flashAttr("tradeEntity", Faker.getFakeTradeEntity()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("rightCreatedTrade", true))
                .andExpect(view().name("redirect:/trade/list"));
    }

    @Test
    @WithMockUser
    public void should_notCreateTrade_whenPostExistingTradeAdd() throws Exception {
        when(tradeService.createTrade(any(TradeEntity.class))).thenThrow(EntityExistsException.class);

        mockMvc.perform(post("/trade/add")
                        .with(csrf())
                        .flashAttr("tradeEntity", Faker.getFakeTradeEntity()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("wrongCreatedTrade", true))
                .andExpect(view().name("/trade/add"));
    }

    @Test
    @WithMockUser
    public void should_returnTradeUpdate_whenGetExistingTradeUpdate() throws Exception {
        when(tradeService.getTradeById(anyInt())).thenReturn(Faker.getFakeTradeEntity());

        mockMvc.perform(get("/trade/update/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/trade/update"));
    }

    @Test
    @WithMockUser
    public void should_notReturnTradeUpdate_whenGetMissingTradeUpdate() throws Exception {
        when(tradeService.getTradeById(anyInt())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/trade/update/1")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("missingTradeId", true))
                .andExpect(view().name("redirect:/trade/list"));
    }

    @Test
    @WithMockUser
    public void should_returnBindingErrors_whenPostPartialTradeUpdate() throws Exception {
        TradeEntity tradeEntityPartial = Faker.getFakeTradeEntity();
        tradeEntityPartial.setAccount("");

        mockMvc.perform(post("/trade/update/1")
                        .with(csrf())
                        .flashAttr("tradeEntity", tradeEntityPartial))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("tradeEntity"))
                .andExpect(view().name("/trade/update"));
    }

    @Test
    @WithMockUser
    public void should_updateTrade_whenPostExistingTradeUpdate() throws Exception {
        when(tradeService.updateTrade(any())).thenReturn(Faker.getFakeTradeEntity());

        mockMvc.perform(post("/trade/update/1")
                        .with(csrf())
                        .flashAttr("tradeEntity", Faker.getFakeTradeEntity()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("rightUpdatedTrade", true))
                .andExpect(view().name("/trade/update"));
    }

    @Test
    @WithMockUser
    public void should_notUpdateTrade_whenPostMissingTradeUpdate() throws Exception {
        when(tradeService.updateTrade((any(TradeEntity.class)))).thenThrow(NoSuchElementException.class);

        mockMvc.perform(post("/trade/update/1")
                        .with(csrf())
                        .flashAttr("tradeEntity", Faker.getFakeTradeEntity()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("missingTradeId", true))
                .andExpect(view().name("redirect:/trade/list"));
    }

    @Test
    @WithMockUser
    public void should_deleteTrade_whenGetExistingTradeDelete() throws Exception {
        mockMvc.perform(get("/trade/delete/1")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("rightDeletedTrade", true))
                .andExpect(view().name("redirect:/trade/list"));
    }

    @Test
    @WithMockUser
    public void should_notDeleteTrade_whenGetMissingTradeDelete() throws Exception {
        doThrow(NoSuchElementException.class).when(tradeService).deleteTradeById(anyInt());

        mockMvc.perform(get("/trade/delete/1")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("missingTradeId", true))
                .andExpect(view().name("redirect:/trade/list"));
    }

}
