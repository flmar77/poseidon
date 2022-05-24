package com.poseidon.app.unittests.web.apiController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poseidon.app.dal.entity.RuleEntity;
import com.poseidon.app.domain.service.RuleService;
import com.poseidon.app.domain.service.UserService;
import com.poseidon.app.helper.Faker;
import com.poseidon.app.web.apiController.RuleRestController;
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

@WebMvcTest(controllers = RuleRestController.class)
public class RuleRestControllerTest {

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
    public void should_returnRightRuleList_whenGetRule() throws Exception {
        when(ruleService.getAllRules()).thenReturn(Collections.singletonList(Faker.getFakeRuleEntity()));

        mockMvc.perform(get("/api/rule"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(Faker.getFakeRuleEntity().getName()))));
    }

    @Test
    public void should_returnRightRule_whenGetRuleId() throws Exception {
        when(ruleService.getRuleById(anyInt())).thenReturn(Faker.getFakeRuleEntity());

        mockMvc.perform(get("/api/rule/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(String.valueOf(Faker.getFakeRuleEntity().getName()))));
    }

    @Test
    public void should_returnNotFound_whenGetMissingRuleId() throws Exception {
        when(ruleService.getRuleById(anyInt())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/api/rule/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("error")));
    }

    @Test
    public void should_returnBindingErrors_whenPostPartialRule() throws Exception {
        RuleEntity ruleEntityPartial = Faker.getFakeRuleEntity();
        ruleEntityPartial.setName("");
        String inputJson = new ObjectMapper().writeValueAsString(ruleEntityPartial);

        mockMvc.perform(post("/api/rule")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_createRule_whenPostNewRule() throws Exception {
        when(ruleService.createRule(any())).thenReturn(Faker.getFakeRuleEntity());
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeRuleEntity());

        mockMvc.perform(post("/api/rule")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void should_notCreateRule_whenPostExistingRule() throws Exception {
        when(ruleService.createRule(any(RuleEntity.class))).thenThrow(EntityExistsException.class);
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeRuleEntity());

        mockMvc.perform(post("/api/rule")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_returnBindingErrors_whenPutPartialRule() throws Exception {
        RuleEntity ruleEntityPartial = Faker.getFakeRuleEntity();
        ruleEntityPartial.setName("");
        String inputJson = new ObjectMapper().writeValueAsString(ruleEntityPartial);

        mockMvc.perform(put("/api/rule/1")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_returnUnprocessableEntity_whenPutInconsistentRule() throws Exception {
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeRuleEntity());

        mockMvc.perform(put("/api/rule/2")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_updateRule_whenPutExistingRule() throws Exception {
        when(ruleService.updateRule(any())).thenReturn(Faker.getFakeRuleEntity());
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeRuleEntity());

        mockMvc.perform(put("/api/rule/1")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void should_notUpdateRule_whenPutMissingRule() throws Exception {
        when(ruleService.updateRule(any())).thenThrow(NoSuchElementException.class);
        String inputJson = new ObjectMapper().writeValueAsString(Faker.getFakeRuleEntity());

        mockMvc.perform(put("/api/rule/1")
                        .content(inputJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_deleteRule_whenDeleteExistingRule() throws Exception {
        mockMvc.perform(delete("/api/rule/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_NotDeleteRule_whenDeleteMissingRule() throws Exception {
        doThrow(new NoSuchElementException()).when(ruleService).deleteRuleById(anyInt());

        mockMvc.perform(delete("/api/rule/1"))
                .andExpect(status().isNotFound());
    }

}
