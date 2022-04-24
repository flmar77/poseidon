package com.poseidon.app.unittests.domain;


import com.poseidon.app.dal.entity.BidEntity;
import com.poseidon.app.dal.repository.BidRepository;
import com.poseidon.app.domain.service.BidService;
import com.poseidon.app.helper.FactoryTest;
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
public class BidServiceTest {

    @InjectMocks
    private BidService bidService;

    @Mock
    private BidRepository bidRepository;

    @Test
    public void should_returnSomething_whenGetAllBids() {
        when(bidRepository.findAll()).thenReturn(Collections.singletonList(FactoryTest.getFakeBidEntity()));

        assertThat(bidService.getAllBids()).isNotNull();
    }

    @Test
    public void should_saveBid_whenCreateNewBid() {
        when(bidRepository.findByAccount(anyString())).thenReturn(Optional.empty());
        when(bidRepository.save(any())).thenReturn(FactoryTest.getFakeBidEntity());

        BidEntity bidEntity = bidService.createBid(FactoryTest.getFakeBidEntity());

        assertThat(bidEntity).isNotNull();
        verify(bidRepository, times(1)).save(any());
    }

    @Test
    public void should_throwEntityExistsException_whenCreateExistingBid() {
        when(bidRepository.findByAccount(anyString())).thenReturn(Optional.of(FactoryTest.getFakeBidEntity()));

        assertThatExceptionOfType(EntityExistsException.class)
                .isThrownBy(() -> bidService.createBid(FactoryTest.getFakeBidEntity()));
    }

    @Test
    public void should_findBid_whenGetExistingBidById() {
        when(bidRepository.findById(anyInt())).thenReturn(Optional.of(FactoryTest.getFakeBidEntity()));

        BidEntity bidEntity = bidService.getBidById(anyInt());

        assertThat(bidEntity).isNotNull();
        verify(bidRepository, times(1)).findById(anyInt());
    }

    @Test
    public void should_throwNoSuchElementException_whenGetExistingBidById() {
        when(bidRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> bidService.getBidById(anyInt()));
    }

    @Test
    public void should_saveBid_whenUpdateExistingBid() {
        when(bidRepository.findById(any())).thenReturn(Optional.of(FactoryTest.getFakeBidEntity()));
        when(bidRepository.save(any())).thenReturn(FactoryTest.getFakeBidEntity());

        BidEntity bidEntity = bidService.updateBid(FactoryTest.getFakeBidEntity());

        assertThat(bidEntity).isNotNull();
        verify(bidRepository, times(1)).save(any());
    }

    @Test
    public void should_throwNoSuchElementException_whenUpdateMissingBid() {
        when(bidRepository.findById(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> bidService.updateBid(FactoryTest.getFakeBidEntity()));
    }

    @Test
    public void should_deleteBid_whenDeleteExistingBid() {
        when(bidRepository.findById(anyInt())).thenReturn(Optional.of(FactoryTest.getFakeBidEntity()));

        bidService.deleteBidById(anyInt());

        verify(bidRepository, times(1)).deleteById(anyInt());
    }

    @Test
    public void should_throwNoSuchElementException_whenDeleteExistingBid() {
        when(bidRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> bidService.deleteBidById(anyInt()));
    }

}
