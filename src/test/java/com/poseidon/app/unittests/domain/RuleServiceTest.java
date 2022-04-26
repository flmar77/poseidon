package com.poseidon.app.unittests.domain;


import com.poseidon.app.dal.entity.RuleEntity;
import com.poseidon.app.dal.repository.RuleRepository;
import com.poseidon.app.domain.service.RuleService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RuleServiceTest {

    @InjectMocks
    private RuleService ruleService;

    @Mock
    private RuleRepository ruleRepository;

    @Test
    public void should_returnSomething_whenGetAllRules() {
        when(ruleRepository.findAll()).thenReturn(Collections.singletonList(Faker.getFakeRuleEntity()));

        assertThat(ruleService.getAllRules()).isNotNull();
    }

    @Test
    public void should_saveRule_whenCreateNewRule() {
        when(ruleRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(ruleRepository.save(any())).thenReturn(Faker.getFakeRuleEntity());

        RuleEntity ruleEntity = ruleService.createRule(Faker.getFakeRuleEntity());

        assertThat(ruleEntity).isNotNull();
        verify(ruleRepository, times(1)).save(any());
    }

    @Test
    public void should_throwEntityExistsException_whenCreateExistingRule() {
        when(ruleRepository.findByName(anyString())).thenReturn(Optional.of(Faker.getFakeRuleEntity()));

        assertThatExceptionOfType(EntityExistsException.class)
                .isThrownBy(() -> ruleService.createRule(Faker.getFakeRuleEntity()));
    }

    @Test
    public void should_findRule_whenGetExistingRuleById() {
        when(ruleRepository.findById(anyInt())).thenReturn(Optional.of(Faker.getFakeRuleEntity()));

        RuleEntity ruleEntity = ruleService.getRuleById(anyInt());

        assertThat(ruleEntity).isNotNull();
        verify(ruleRepository, times(1)).findById(anyInt());
    }

    @Test
    public void should_throwNoSuchElementException_whenGetExistingRuleById() {
        when(ruleRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> ruleService.getRuleById(anyInt()));
    }

    @Test
    public void should_saveRule_whenUpdateExistingRule() {
        when(ruleRepository.findById(any())).thenReturn(Optional.of(Faker.getFakeRuleEntity()));
        when(ruleRepository.save(any())).thenReturn(Faker.getFakeRuleEntity());

        RuleEntity ruleEntity = ruleService.updateRule(Faker.getFakeRuleEntity());

        assertThat(ruleEntity).isNotNull();
        verify(ruleRepository, times(1)).save(any());
    }

    @Test
    public void should_throwNoSuchElementException_whenUpdateMissingRule() {
        when(ruleRepository.findById(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> ruleService.updateRule(Faker.getFakeRuleEntity()));
    }

    @Test
    public void should_deleteRule_whenDeleteExistingRule() {
        when(ruleRepository.findById(anyInt())).thenReturn(Optional.of(Faker.getFakeRuleEntity()));

        ruleService.deleteRuleById(anyInt());

        verify(ruleRepository, times(1)).deleteById(anyInt());
    }

    @Test
    public void should_throwNoSuchElementException_whenDeleteExistingRule() {
        when(ruleRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> ruleService.deleteRuleById(anyInt()));
    }

}
