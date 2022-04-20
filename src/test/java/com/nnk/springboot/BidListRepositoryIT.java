package com.nnk.springboot;

import com.nnk.springboot.dal.entity.BidListEntity;
import com.nnk.springboot.dal.repository.BidListRepository;
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
public class BidListRepositoryIT {

	@Autowired
	private BidListRepository bidListRepository;

	@Test
	public void bidListTest() {
		BidListEntity bid = new BidListEntity("Account Test", "Type Test", 10d);

		// Save
		bid = bidListRepository.save(bid);
		Assert.assertNotNull(bid.getBidListId());
		Assert.assertEquals(bid.getBidQuantity(), 10d, 10d);

		// Update
		bid.setBidQuantity(20d);
		bid = bidListRepository.save(bid);
		Assert.assertEquals(bid.getBidQuantity(), 20d, 20d);

		// Find
		List<BidListEntity> listResult = bidListRepository.findAll();
		Assert.assertTrue(listResult.size() > 0);

		// Delete
		Integer id = bid.getBidListId();
		bidListRepository.delete(bid);
		Optional<BidListEntity> bidList = bidListRepository.findById(id);
		Assert.assertFalse(bidList.isPresent());
	}
}
