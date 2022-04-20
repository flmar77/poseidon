package com.nnk.springboot;

import com.nnk.springboot.dal.entity.CurvePointEntity;
import com.nnk.springboot.dal.repository.CurvePointRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CurvePointRepositoryIT {

	@Autowired
	private CurvePointRepository curvePointRepository;

	@Test
	public void curvePointTest() {
		CurvePointEntity curvePointEntity = new CurvePointEntity(10, 10d, 30d);

		// Save
		curvePointEntity = curvePointRepository.save(curvePointEntity);
		Assert.assertNotNull(curvePointEntity.getId());
		Assert.assertTrue(curvePointEntity.getCurveId() == 10);

		// Update
		curvePointEntity.setCurveId(20);
		curvePointEntity = curvePointRepository.save(curvePointEntity);
		Assert.assertTrue(curvePointEntity.getCurveId() == 20);

		// Find
		List<CurvePointEntity> listResult = curvePointRepository.findAll();
		Assert.assertTrue(listResult.size() > 0);

		// Delete
		Integer id = curvePointEntity.getId();
		curvePointRepository.delete(curvePointEntity);
		Optional<CurvePointEntity> curvePointList = curvePointRepository.findById(id);
		Assert.assertFalse(curvePointList.isPresent());
	}

}
