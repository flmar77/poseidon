package com.poseidon.app.unittests.web;

import com.poseidon.app.dal.entity.RuleEntity;
import com.poseidon.app.domain.service.RuleService;
import com.poseidon.app.domain.service.UserService;
import com.poseidon.app.helper.Faker;
import com.poseidon.app.web.frontController.RuleController;
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

@WebMvcTest(controllers = RuleController.class)
public class RuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RuleService ruleService;

    @SuppressWarnings("unused")
    @MockBean
    private UserService userService;
    @SuppressWarnings("unused")
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser
    public void should_returnRightRuleList_whenGetRuleList() throws Exception {
        when(ruleService.getAllRules()).thenReturn(Collections.singletonList(Faker.getFakeRuleEntity()));

        mockMvc.perform(get("/rule/list")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/rule/list"))
                .andExpect(model().attributeExists("ruleEntities"))
                .andExpect(content().string(containsString(String.valueOf(Faker.getFakeRuleEntity().getName()))));
    }

    @Test
    @WithMockUser
    public void should_returnRightRuleAdd_whenGetRuleAdd() throws Exception {
        mockMvc.perform(get("/rule/add")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ruleEntity"))
                .andExpect(view().name("/rule/add"));
    }

    @Test
    @WithMockUser
    public void should_returnBindingErrors_whenPostPartialRuleAdd() throws Exception {
        RuleEntity ruleEntityPartial = Faker.getFakeRuleEntity();
        ruleEntityPartial.setName("");

        mockMvc.perform(post("/rule/add")
                        .with(csrf())
                        .flashAttr("ruleEntity", ruleEntityPartial))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("ruleEntity"))
                .andExpect(view().name("/rule/add"));
    }

    @Test
    @WithMockUser
    public void should_createRule_whenPostNewRuleAdd() throws Exception {
        when(ruleService.createRule(any())).thenReturn(Faker.getFakeRuleEntity());

        mockMvc.perform(post("/rule/add")
                        .with(csrf())
                        .flashAttr("ruleEntity", Faker.getFakeRuleEntity()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("rightCreatedRule", true))
                .andExpect(view().name("redirect:/rule/list"));
    }

    @Test
    @WithMockUser
    public void should_notCreateRule_whenPostExistingRuleAdd() throws Exception {
        when(ruleService.createRule(any(RuleEntity.class))).thenThrow(EntityExistsException.class);

        mockMvc.perform(post("/rule/add")
                        .with(csrf())
                        .flashAttr("ruleEntity", Faker.getFakeRuleEntity()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("wrongCreatedRule", true))
                .andExpect(view().name("/rule/add"));
    }

    @Test
    @WithMockUser
    public void should_returnRuleUpdate_whenGetExistingRuleUpdate() throws Exception {
        when(ruleService.getRuleById(anyInt())).thenReturn(Faker.getFakeRuleEntity());

        mockMvc.perform(get("/rule/update/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/rule/update"));
    }

    @Test
    @WithMockUser
    public void should_notReturnRuleUpdate_whenGetMissingRuleUpdate() throws Exception {
        when(ruleService.getRuleById(anyInt())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/rule/update/1")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("missingRuleId", true))
                .andExpect(view().name("redirect:/rule/list"));
    }

    @Test
    @WithMockUser
    public void should_returnBindingErrors_whenPostPartialRuleUpdate() throws Exception {
        RuleEntity ruleEntityPartial = Faker.getFakeRuleEntity();
        ruleEntityPartial.setName("");

        mockMvc.perform(post("/rule/update/1")
                        .with(csrf())
                        .flashAttr("ruleEntity", ruleEntityPartial))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("ruleEntity"))
                .andExpect(view().name("/rule/update"));
    }

    @Test
    @WithMockUser
    public void should_updateRule_whenPostExistingRuleUpdate() throws Exception {
        when(ruleService.updateRule(any())).thenReturn(Faker.getFakeRuleEntity());

        mockMvc.perform(post("/rule/update/1")
                        .with(csrf())
                        .flashAttr("ruleEntity", Faker.getFakeRuleEntity()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.model().attribute("rightUpdatedRule", true))
                .andExpect(view().name("/rule/update"));
    }

    @Test
    @WithMockUser
    public void should_notUpdateRule_whenPostMissingRuleUpdate() throws Exception {
        when(ruleService.updateRule((any(RuleEntity.class)))).thenThrow(NoSuchElementException.class);

        mockMvc.perform(post("/rule/update/1")
                        .with(csrf())
                        .flashAttr("ruleEntity", Faker.getFakeRuleEntity()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("missingRuleId", true))
                .andExpect(view().name("redirect:/rule/list"));
    }

    @Test
    @WithMockUser
    public void should_deleteRule_whenGetExistingRuleDelete() throws Exception {
        mockMvc.perform(get("/rule/delete/1")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("rightDeletedRule", true))
                .andExpect(view().name("redirect:/rule/list"));
    }

    @Test
    @WithMockUser
    public void should_notDeleteRule_whenGetMissingRuleDelete() throws Exception {
        doThrow(NoSuchElementException.class).when(ruleService).deleteRuleById(anyInt());

        mockMvc.perform(get("/rule/delete/1")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.flash().attribute("missingRuleId", true))
                .andExpect(view().name("redirect:/rule/list"));
    }

}
