package com.poseidon.app.unittests.domain;


import com.poseidon.app.dal.entity.CurveEntity;
import com.poseidon.app.dal.repository.CurveRepository;
import com.poseidon.app.domain.service.CurveService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurveServiceTest {

    @InjectMocks
    private CurveService curveService;

    @Mock
    private CurveRepository curveRepository;

    @Test
    public void should_returnSomething_whenGetAllCurves() {
        when(curveRepository.findAll()).thenReturn(Collections.singletonList(Faker.getFakeCurveEntity()));

        assertThat(curveService.getAllCurves()).isNotNull();
    }

    @Test
    public void should_saveCurve_whenCreateNewCurve() {
        when(curveRepository.findByCurveId(anyInt())).thenReturn(Optional.empty());
        when(curveRepository.save(any())).thenReturn(Faker.getFakeCurveEntity());

        CurveEntity curveEntity = curveService.createCurve(Faker.getFakeCurveEntity());

        assertThat(curveEntity).isNotNull();
        verify(curveRepository, times(1)).save(any());
    }

    @Test
    public void should_throwEntityExistsException_whenCreateExistingCurve() {
        when(curveRepository.findByCurveId(anyInt())).thenReturn(Optional.of(Faker.getFakeCurveEntity()));

        assertThatExceptionOfType(EntityExistsException.class)
                .isThrownBy(() -> curveService.createCurve(Faker.getFakeCurveEntity()));
    }

    @Test
    public void should_findCurve_whenGetExistingCurveById() {
        when(curveRepository.findById(anyInt())).thenReturn(Optional.of(Faker.getFakeCurveEntity()));

        CurveEntity curveEntity = curveService.getCurveById(anyInt());

        assertThat(curveEntity).isNotNull();
        verify(curveRepository, times(1)).findById(anyInt());
    }

    @Test
    public void should_throwNoSuchElementException_whenGetExistingCurveById() {
        when(curveRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> curveService.getCurveById(anyInt()));
    }

    @Test
    public void should_saveCurve_whenUpdateExistingCurve() {
        when(curveRepository.findById(any())).thenReturn(Optional.of(Faker.getFakeCurveEntity()));
        when(curveRepository.save(any())).thenReturn(Faker.getFakeCurveEntity());

        CurveEntity curveEntity = curveService.updateCurve(Faker.getFakeCurveEntity());

        assertThat(curveEntity).isNotNull();
        verify(curveRepository, times(1)).save(any());
    }

    @Test
    public void should_throwNoSuchElementException_whenUpdateMissingCurve() {
        when(curveRepository.findById(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> curveService.updateCurve(Faker.getFakeCurveEntity()));
    }

    @Test
    public void should_deleteCurve_whenDeleteExistingCurve() {
        when(curveRepository.findById(anyInt())).thenReturn(Optional.of(Faker.getFakeCurveEntity()));

        curveService.deleteCurveById(anyInt());

        verify(curveRepository, times(1)).deleteById(anyInt());
    }

    @Test
    public void should_throwNoSuchElementException_whenDeleteExistingCurve() {
        when(curveRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> curveService.deleteCurveById(anyInt()));
    }

}
