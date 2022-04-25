package com.poseidon.app.domain.service;

import com.poseidon.app.dal.entity.CurveEntity;
import com.poseidon.app.dal.repository.CurveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CurveService {

    @Autowired
    private CurveRepository curveRepository;

    public List<CurveEntity> getAllCurves() {
        return curveRepository.findAll();
    }

    public CurveEntity createCurve(CurveEntity curveEntity) throws EntityExistsException {
        if (curveRepository.findByCurveId(curveEntity.getCurveId()).isPresent()) {
            throw new EntityExistsException();
        }
        return saveCurve(curveEntity);
    }

    public CurveEntity getCurveById(Integer id) throws NoSuchElementException {
        return curveRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public CurveEntity updateCurve(CurveEntity curveEntity) throws NoSuchElementException {
        checkExistenceOfCurveById(curveEntity.getId());
        return saveCurve(curveEntity);
    }

    public void deleteCurveById(Integer id) throws NoSuchElementException {
        checkExistenceOfCurveById(id);
        curveRepository.deleteById(id);
    }

    private void checkExistenceOfCurveById(Integer id) {
        curveRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    private CurveEntity saveCurve(CurveEntity curveEntityToSave) {
        return curveRepository.save(curveEntityToSave);
    }
}
