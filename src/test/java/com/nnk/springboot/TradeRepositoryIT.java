package com.nnk.springboot;

import com.nnk.springboot.dal.entity.TradeEntity;
import com.nnk.springboot.dal.repository.TradeRepository;
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
public class TradeRepositoryIT {

	@Autowired
	private TradeRepository tradeRepository;

	@Test
	public void tradeTest() {
		TradeEntity tradeEntity = new TradeEntity("Trade Account", "Type");

		// Save
		tradeEntity = tradeRepository.save(tradeEntity);
		Assert.assertNotNull(tradeEntity.getTradeId());
		Assert.assertTrue(tradeEntity.getAccount().equals("Trade Account"));

		// Update
		tradeEntity.setAccount("Trade Account Update");
		tradeEntity = tradeRepository.save(tradeEntity);
		Assert.assertTrue(tradeEntity.getAccount().equals("Trade Account Update"));

		// Find
		List<TradeEntity> listResult = tradeRepository.findAll();
		Assert.assertTrue(listResult.size() > 0);

		// Delete
		Integer id = tradeEntity.getTradeId();
		tradeRepository.delete(tradeEntity);
		Optional<TradeEntity> tradeList = tradeRepository.findById(id);
		Assert.assertFalse(tradeList.isPresent());
	}
}
