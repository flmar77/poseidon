package com.poseidon.app.unittests.domain;


import com.poseidon.app.dal.entity.TradeEntity;
import com.poseidon.app.dal.repository.TradeRepository;
import com.poseidon.app.domain.service.TradeService;
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
public class TradeServiceTest {

    @InjectMocks
    private TradeService tradeService;

    @Mock
    private TradeRepository tradeRepository;

    @Test
    public void should_returnSomething_whenGetAllTrades() {
        when(tradeRepository.findAll()).thenReturn(Collections.singletonList(Faker.getFakeTradeEntity()));

        assertThat(tradeService.getAllTrades()).isNotNull();
    }

    @Test
    public void should_saveTrade_whenCreateNewTrade() {
        when(tradeRepository.findByAccount(anyString())).thenReturn(Optional.empty());
        when(tradeRepository.save(any())).thenReturn(Faker.getFakeTradeEntity());

        TradeEntity tradeEntity = tradeService.createTrade(Faker.getFakeTradeEntity());

        assertThat(tradeEntity).isNotNull();
        verify(tradeRepository, times(1)).save(any());
    }

    @Test
    public void should_throwEntityExistsException_whenCreateExistingTrade() {
        when(tradeRepository.findByAccount(anyString())).thenReturn(Optional.of(Faker.getFakeTradeEntity()));

        assertThatExceptionOfType(EntityExistsException.class)
                .isThrownBy(() -> tradeService.createTrade(Faker.getFakeTradeEntity()));
    }

    @Test
    public void should_findTrade_whenGetExistingTradeById() {
        when(tradeRepository.findById(anyInt())).thenReturn(Optional.of(Faker.getFakeTradeEntity()));

        TradeEntity tradeEntity = tradeService.getTradeById(anyInt());

        assertThat(tradeEntity).isNotNull();
        verify(tradeRepository, times(1)).findById(anyInt());
    }

    @Test
    public void should_throwNoSuchElementException_whenGetExistingTradeById() {
        when(tradeRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> tradeService.getTradeById(anyInt()));
    }

    @Test
    public void should_saveTrade_whenUpdateExistingTrade() {
        when(tradeRepository.findById(any())).thenReturn(Optional.of(Faker.getFakeTradeEntity()));
        when(tradeRepository.save(any())).thenReturn(Faker.getFakeTradeEntity());

        TradeEntity tradeEntity = tradeService.updateTrade(Faker.getFakeTradeEntity());

        assertThat(tradeEntity).isNotNull();
        verify(tradeRepository, times(1)).save(any());
    }

    @Test
    public void should_throwNoSuchElementException_whenUpdateMissingTrade() {
        when(tradeRepository.findById(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> tradeService.updateTrade(Faker.getFakeTradeEntity()));
    }

    @Test
    public void should_deleteTrade_whenDeleteExistingTrade() {
        when(tradeRepository.findById(anyInt())).thenReturn(Optional.of(Faker.getFakeTradeEntity()));

        tradeService.deleteTradeById(anyInt());

        verify(tradeRepository, times(1)).deleteById(anyInt());
    }

    @Test
    public void should_throwNoSuchElementException_whenDeleteExistingTrade() {
        when(tradeRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> tradeService.deleteTradeById(anyInt()));
    }

}
