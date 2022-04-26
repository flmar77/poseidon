package com.poseidon.app.integrationtests;

import com.poseidon.app.dal.entity.CurveEntity;
import com.poseidon.app.dal.repository.CurveRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CurveRepositoryIT {

    @Autowired
    private CurveRepository curvePointRepository;

    @Test
    public void curvePointTest() {
        CurveEntity curveEntity = new CurveEntity(10, 10d, 30d);

        // Save
        curveEntity = curvePointRepository.save(curveEntity);
        assertNotNull(curveEntity.getId());
        assertTrue(curveEntity.getCurveId() == 10);

        // Update
        curveEntity.setCurveId(20);
        curveEntity = curvePointRepository.save(curveEntity);
        assertTrue(curveEntity.getCurveId() == 20);

        // Find
        List<CurveEntity> listResult = curvePointRepository.findAll();
        assertTrue(listResult.size() > 0);

        // Delete
        Integer id = curveEntity.getId();
        curvePointRepository.delete(curveEntity);
        Optional<CurveEntity> curvePointList = curvePointRepository.findById(id);
        assertFalse(curvePointList.isPresent());
    }

}
