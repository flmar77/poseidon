package com.poseidon.app.unittests.web;

import com.poseidon.app.dal.entity.BidEntity;
import com.poseidon.app.domain.service.BidService;
import com.poseidon.app.domain.service.UserService;
import com.poseidon.app.helper.FactoryTest;
import com.poseidon.app.web.frontController.BidController;
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

@WebMvcTest(controllers = BidController.class)
public class BidControllerTest {

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
    @WithMockUser
    public void should_returnRightBidList_whenGetBidList() throws Exception {
        when(bidService.getAllBids()).thenReturn(Collections.singletonList(FactoryTest.getFakeBidEntity()));

        mockMvc.perform(get("/bid/list")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/bid/list"))
                .andExpect(model().attributeExists("bidEntities"))
                .andExpect(content().string(containsString("accountTest")));
    }

    @Test
    @WithMockUser
    public void should_returnRightBidAdd_whenGetBidAdd() throws Exception {
        mockMvc.perform(get("/bid/add")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bidEntity"))
                .andExpect(view().name("/bid/add"));
    }

    @Test
    @WithMockUser
    public void should_returnBindingErrors_whenPostPartialBidAdd() throws Exception {
        BidEntity bidEntityPartial = FactoryTest.getFakeBidEntity();
        bidEntityPartial.setAccount("");

        mockMvc.perform(post("/bid/add")
                        .with(csrf())
                        .flashAttr("bidEntity", bidEntityPartial))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("bidEntity"))
                .andExpect(view().name("/bid/add"));
    }

    @Test
    @WithMockUser
    public void should_createBid_whenPostNewBidAdd() throws Exception {
        when(bidService.createBid(any())).thenReturn(FactoryTest.getFakeBidEntity());

        mockMvc.perform(post("/bid/add")
                        .with(csrf())
                        .flashAttr("bidEntity", FactoryTest.getFakeBidEntity()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("rightCreatedBid", true))
                .andExpect(view().name("redirect:/bid/list"));
    }

    @Test
    @WithMockUser
    public void should_notCreateBid_whenPostExistingBidAdd() throws Exception {
        when(bidService.createBid(any(BidEntity.class))).thenThrow(EntityExistsException.class);

        mockMvc.perform(post("/bid/add")
                        .with(csrf())
                        .flashAttr("bidEntity", FactoryTest.getFakeBidEntity()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("wrongCreatedBid", true))
                .andExpect(view().name("/bid/add"));
    }

    @Test
    @WithMockUser
    public void should_returnBidUpdate_whenGetExistingBidUpdate() throws Exception {
        when(bidService.getBidById(anyInt())).thenReturn(FactoryTest.getFakeBidEntity());

        mockMvc.perform(get("/bid/update/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/bid/update"));
    }

    @Test
    @WithMockUser
    public void should_notReturnBidUpdate_whenGetMissingBidUpdate() throws Exception {
        when(bidService.getBidById(anyInt())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/bid/update/1")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("missingBidId", true))
                .andExpect(view().name("redirect:/bid/list"));
    }

    @Test
    @WithMockUser
    public void should_returnBindingErrors_whenPostPartialBidUpdate() throws Exception {
        BidEntity bidEntityPartial = FactoryTest.getFakeBidEntity();
        bidEntityPartial.setAccount("");

        mockMvc.perform(post("/bid/update/1")
                        .with(csrf())
                        .flashAttr("bidEntity", bidEntityPartial))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("bidEntity"))
                .andExpect(view().name("/bid/update"));
    }

    @Test
    @WithMockUser
    public void should_updateBid_whenPostExistingBidUpdate() throws Exception {
        when(bidService.updateBid(any())).thenReturn(FactoryTest.getFakeBidEntity());

        mockMvc.perform(post("/bid/update/1")
                        .with(csrf())
                        .flashAttr("bidEntity", FactoryTest.getFakeBidEntity()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("rightUpdatedBid", true))
                .andExpect(view().name("/bid/update"));
    }

    @Test
    @WithMockUser
    public void should_notUpdateBid_whenPostMissingBidUpdate() throws Exception {
        when(bidService.updateBid((any(BidEntity.class)))).thenThrow(NoSuchElementException.class);

        mockMvc.perform(post("/bid/update/1")
                        .with(csrf())
                        .flashAttr("bidEntity", FactoryTest.getFakeBidEntity()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("missingBidId", true))
                .andExpect(view().name("redirect:/bid/list"));
    }

    @Test
    @WithMockUser
    public void should_deleteBid_whenGetExistingBidDelete() throws Exception {
        mockMvc.perform(get("/bid/delete/1")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("rightDeletedBid", true))
                .andExpect(view().name("redirect:/bid/list"));
    }

    @Test
    @WithMockUser
    public void should_notDeleteBid_whenGetMissingBidDelete() throws Exception {
        doThrow(NoSuchElementException.class).when(bidService).deleteBidById(anyInt());

        mockMvc.perform(get("/bid/delete/1")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("missingBidId", true))
                .andExpect(view().name("redirect:/bid/list"));
    }

}
