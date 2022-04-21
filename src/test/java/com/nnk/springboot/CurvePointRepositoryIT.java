package com.nnk.springboot;

import com.nnk.springboot.dal.entity.CurvePointEntity;
import com.nnk.springboot.dal.repository.CurvePointRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CurvePointRepositoryIT {

    @Autowired
    private CurvePointRepository curvePointRepository;

    @Test
    public void curvePointTest() {
        CurvePointEntity curvePointEntity = new CurvePointEntity(10, 10d, 30d);

        // Save
        curvePointEntity = curvePointRepository.save(curvePointEntity);
        assertNotNull(curvePointEntity.getId());
        assertTrue(curvePointEntity.getCurveId() == 10);

        // Update
        curvePointEntity.setCurveId(20);
        curvePointEntity = curvePointRepository.save(curvePointEntity);
        assertTrue(curvePointEntity.getCurveId() == 20);

        // Find
        List<CurvePointEntity> listResult = curvePointRepository.findAll();
        assertTrue(listResult.size() > 0);

        // Delete
        Integer id = curvePointEntity.getId();
        curvePointRepository.delete(curvePointEntity);
        Optional<CurvePointEntity> curvePointList = curvePointRepository.findById(id);
        assertFalse(curvePointList.isPresent());
    }

}
